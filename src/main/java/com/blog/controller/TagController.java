package com.blog.controller;

import com.blog.Tag;
import com.blog.model.Page;
import com.blog.service.TagRestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        Page page = new Page();
        page.setSearch(true);
        page.setTitle("Categories page");
        page.setMenuItem("blog");

        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        return "blogCategories";
    }
}
