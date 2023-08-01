package com.example.treehole.Service;

import com.example.treehole.Util.RedisKeyUtil;
import com.example.treehole.Util.TreeholeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 查询关注的实体的数量，实体不仅仅是用户，也可以是帖子、评论
    public long findFolloweeCount(int userId, int entityType) {
        String key = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(key);
    }

    // 查询实体的粉丝的数量
    public long findFollowerCount(int entityType, int userId) {
        String key = RedisKeyUtil.getFollowerKey(entityType, userId);
        return redisTemplate.opsForZSet().zCard(key);
    }

    // 查询当前用户是否已关注该实体
    public boolean hasFollowed(int userId, int entityId, int entityType) {
        String key = RedisKeyUtil.getFolloweeKey(userId, entityType);
        // 如果score查询不为空则已关注，返回布尔值为 1
        return redisTemplate.opsForZSet().score(key, entityId) != null;
    }

    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 关注者 和 被关注者要相应修改 关注数 和 被关注数
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();
                operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();
                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);

                return operations.exec();
            }
        });
    }
}
