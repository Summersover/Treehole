package com.example.treehole.Controller;

import com.example.treehole.Entity.User;
import com.example.treehole.Service.FollowService;
import com.example.treehole.Util.HostHolder;
import com.example.treehole.Util.TreeholeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        return TreeholeUtil.getJsonString(0, "关注成功");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return TreeholeUtil.getJsonString(0, "取消关注成功");
    }

}
