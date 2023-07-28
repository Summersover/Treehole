package com.example.treehole.Controller;

import com.example.treehole.Entity.Comment;
import com.example.treehole.Entity.DiscussPost;
import com.example.treehole.Entity.Page;
import com.example.treehole.Entity.User;
import com.example.treehole.Service.CommentService;
import com.example.treehole.Service.DiscussPostService;
import com.example.treehole.Service.UserService;
import com.example.treehole.Util.HostHolder;
import com.example.treehole.Util.TreeholeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.treehole.Util.TreeholeConstant.ENTITY_TYPE_COMMENT;
import static com.example.treehole.Util.TreeholeConstant.ENTITY_TYPE_POST;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

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
    public String findDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());
        // 评论
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment);
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 楼中楼
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复对象
                        int targetId = reply.getTargetId();
                        User target = targetId == 0 ? null : userService.findUserById(targetId);
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                // 回复和回复数
                commentVo.put("replys",replyVoList);
                commentVo.put("replyCount",
                        commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId()));

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }

}
