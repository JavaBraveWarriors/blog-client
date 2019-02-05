package com.blog.controller;

import com.blog.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(Model model) {
        Page page = new Page();
        page.setMenuItem("main");
        page.setSearch(true);
        model.addAttribute("page", page);
        return "index.html";
    }
}
