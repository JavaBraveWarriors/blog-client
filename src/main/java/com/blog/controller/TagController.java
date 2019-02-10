package com.blog.controller;

import com.blog.model.Tag;
import com.blog.service.TagRestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class TagController {

    private TagRestClientService tagService;

    @Autowired
    public TagController(TagRestClientService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public String categories(Model model) {
        List<Tag> tags = tagService.getAllTags();
        Map<String, String> page = new HashMap<>();
        page.put("search", "true");
        page.put("title", "Categories page");
        page.put("menuItem", "blog");

        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        return "blogCategories";
    }
}
