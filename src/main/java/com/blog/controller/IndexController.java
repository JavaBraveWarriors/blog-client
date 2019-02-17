package com.blog.controller;

import com.blog.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(Model model) {
        Map<String, String> page = Page.getPageDefaultParams();
        page.put("activeTab", "main");
        model.addAttribute("page", page);

        return "index.html";
    }
}
