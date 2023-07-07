package com.example.treehole.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/alpha")
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello SpringBoot!";
    }
}
