package com.example.treehole.Controller;

import com.example.treehole.Service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/alpha")
public class HelloController {
    @Autowired
    private AlphaService alphaService;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello SpringBoot!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }
}
