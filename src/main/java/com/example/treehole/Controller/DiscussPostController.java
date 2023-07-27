package com.example.treehole.Controller;

import com.example.treehole.Entity.DiscussPost;
import com.example.treehole.Entity.User;
import com.example.treehole.Service.DiscussPostService;
import com.example.treehole.Service.UserService;
import com.example.treehole.Util.HostHolder;
import com.example.treehole.Util.TreeholeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return TreeholeUtil.getJsonString(403, "请先登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        // 报错在其他地方统一处理
        return TreeholeUtil.getJsonString(0, "帖子发布成功");
    }

    @GetMapping("/detail/{discussPostId}")
    public String findDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        return "/site/discuss-detail";
    }

}
