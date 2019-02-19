package com.blog.controller;

import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

// TODO: refactor this class with using ajax.
@Controller
@RequestMapping("/user/cabinet")
public class CabinetController {

    private PageService pageService;

    @Autowired
    public CabinetController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/statistics")
    public String getCabinetStatistic(Model model) {

        Map<String, String> page = pageService.getPageDefaultParams();

        page.put("currentPage", "/statistics");
        model.addAttribute("page", page);
        return "cabinet";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {

        Map<String, String> page = pageService.getPageDefaultParams();
        page.put("currentPage", "/profile");
        model.addAttribute("page", page);
        return "cabinet";
    }
}
