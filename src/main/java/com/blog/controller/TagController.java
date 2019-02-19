package com.blog.controller;

import com.blog.model.ActiveUser;
import com.blog.model.Tag;
import com.blog.service.PageService;
import com.blog.service.TagRestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class TagController {

    private TagRestClientService tagService;
    private PageService pageService;

    @Autowired
    public TagController(PageService pageService, TagRestClientService tagService) {
        this.tagService = tagService;
        this.pageService = pageService;
    }

    @GetMapping("")
    public String categories(Model model) {
        List<Tag> tags = tagService.getAllTags();
        Map<String, String> page = pageService.getPageDefaultParams();
        page.put("title", "Categories page");

        setActiveUserInModelAttribute(model);

        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        return "blogCategories";
    }

    //TODO: refactor this when will be security. UserId must back from rest-api with every request.
    private void setActiveUserInModelAttribute(Model model) {
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        model.addAttribute("user", user);
    }
}
