package com.blog.controller;

import com.blog.model.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/user/cabinet")
public class CabinetController {

    @GetMapping("/statistics")
    public String getCabinetStatistic(Model model) {

        Map<String, String> page = Page.getPageDafaultParams();

        page.put("currentPage", "/statistics");
        model.addAttribute("page", page);
        return "cabinet";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {

        Map<String, String> page = Page.getPageDafaultParams();
        page.put("currentPage", "/profile");
        model.addAttribute("page", page);
        return "cabinet";
    }
}
