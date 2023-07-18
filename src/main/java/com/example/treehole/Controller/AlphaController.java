package com.example.treehole.Controller;

import com.example.treehole.Service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        return "Hello SpringBoot!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 打印请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        // 取出所有请求头名，Enumeration枚举和iteration迭代器类似
        // 但getHeaderNames()只能用enumeration接收而不是可迭代对象
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8"); // 设置返回类型为网页
        try (PrintWriter writer = response.getWriter()) {
            //打印网页，try小括号内对象会自动执行final关闭
            writer.write("<h1>树洞</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GET请求：/students?current=1&limit=20

    // @RequestMapping(path = "/students", method = RequestMethod.GET)
    // RequestMapping在指定路径外，还可以指定唯一的访问方法，这一句等同于GetMapping
    @GetMapping("/students")
    @ResponseBody
    // 传参可以用RequestParam注解的不同字段进行限制
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "Some Students";
    }

    @GetMapping("/student/{id}")
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    // POST请求
    // 使用静态网页传数据，路径为/treehole/html/student.html
    @PostMapping("/student")
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "Success";
    }

    // 响应HTML数据
    @GetMapping("/teacher")
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "风栖绿桂");
        modelAndView.addObject("age", 123);
        modelAndView.setViewName("/demo/view");
        return modelAndView; // 需要预先写明模板文件
    }

    @GetMapping("/school") // 等同于上一个方法
    public String getSchool(Model model) {
        model.addAttribute("name", "抹茶葡提");
        model.addAttribute("age", 321);
        return "/demo/view";
    }

    // 响应JSON对象（异步请求）
    // Java对象 -> JSON字符串 -> JS对象
    @GetMapping("/employee")
    @ResponseBody
    public List<Map<String, Object>> getEmployee() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("name", "声声乌龙");
        map.put("age", 23);
        map.put("price", 16);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "人间烟火");
        map.put("age", 56);
        map.put("price", 18);
        list.add(map);

        map = new HashMap<>();
        map.put("name", "少年时");
        map.put("age", 14);
        map.put("price", 15);
        list.add(map);

        return list;
    }

}
