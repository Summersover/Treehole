package com.example.treehole.DAO;

import com.example.treehole.Entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // offset是当前页（可能不是第一页）的起始帖子条数
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //如果只有一个参数，并且在<if>里使用，则必须用@param注解加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id,int commentCount);

}
