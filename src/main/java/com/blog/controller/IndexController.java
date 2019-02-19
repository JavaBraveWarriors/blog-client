package com.blog.controller;

import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class IndexController {

    private PageService pageService;

    @Autowired
    public IndexController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        Map<String, String> page = pageService.getPageDefaultParams();
        page.put("activeTab", "main");
        model.addAttribute("page", page);

        return "index";
    }
}
