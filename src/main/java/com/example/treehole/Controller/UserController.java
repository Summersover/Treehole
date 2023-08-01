package com.example.treehole.Controller;

import com.example.treehole.Annotation.LoginRequired;
import com.example.treehole.Entity.User;
import com.example.treehole.Service.FollowService;
import com.example.treehole.Service.LikeService;
import com.example.treehole.Service.UserService;
import com.example.treehole.Util.HostHolder;
import com.example.treehole.Util.TreeholeConstant;
import com.example.treehole.Util.TreeholeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements TreeholeConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${treehole.path.upload}")
    private String uploadPath;

    @Value("${treehole.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        // 从文件名中截取后缀以暂存
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确");
            return "/site/setting";
        }
        // 生成随机文件名
        filename = TreeholeUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传失败，服务器异常", e);
        }
        // 更新用户头像资源的访问路径
        // localhost:8080/treehole/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response) {
        // 服务器存放路径
        filename = uploadPath + "/" + filename;
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try (FileInputStream fis = new FileInputStream(filename);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int b;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        }
    }

    @LoginRequired
    @PostMapping("/updatePassword")
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();
        int userId = user.getId();
        Map<String, Object> map = userService.updatePassword(userId, oldPassword, newPassword);
        // 如果map里没有消息则没有报错，重定向到退出界面
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        } else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting"; // 报错则返回原设置界面
        }
    }

    @GetMapping("profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        // 可以访问任意用户的首页
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        // 该用户的赞数
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("user", user);
        model.addAttribute("likeCount", likeCount);

        // 关注数
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

}
