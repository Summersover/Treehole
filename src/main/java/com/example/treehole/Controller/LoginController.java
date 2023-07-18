package com.example.treehole.Controller;

import com.example.treehole.Entity.User;
import com.example.treehole.Service.UserService;
import com.example.treehole.Util.TreeholeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController implements TreeholeConstant {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegsiterPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件。");
            model.addAttribute("target", "/index"); //跳转到index页面即首页
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }

    }

    // http://localhost:8080/treehole/activation/userId/activationCode
    @GetMapping("/actication/{userId}/{activationCode}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("activationCode") String activationCode) {
        int result = userService.activation(userId, activationCode);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "账号激活成功");
            model.addAttribute("target", "/login");
        } else if (result==ACTIVATION_REPEAT) {
            model.addAttribute("msg", "该账号已激活");
            model.addAttribute("target", "/index");
        }else {
            model.addAttribute("msg", "激活码错误");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }
}
