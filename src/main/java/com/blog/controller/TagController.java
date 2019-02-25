package com.blog.controller;

import com.blog.dao.TagDao;
import com.blog.model.ActiveUser;
import com.blog.model.Tag;
import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class TagController {

    private TagDao tagDao;
    private PageService pageService;

    @Autowired
    public TagController(PageService pageService, TagDao tagDao) {
        this.tagDao = tagDao;
        this.pageService = pageService;
    }

    @GetMapping("")
    public String categories(Model model) {
        List<Tag> tags = tagDao.getAllTags();
        Map<String, String> page = pageService.getPageDefaultParams();
        page.put("title", "Categories page");

        model.addAttribute("user", getActiveUser());
        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        return "blogCategories";
    }

    @GetMapping("/tags/add")
    public String getModalForAddTag(Model model) {
        Tag tag = new Tag();
        Map<String, String> page = pageService.getPageDefaultParams();
        page.put("title", "add");
        model.addAttribute("tag", tag);
        model.addAttribute("page", page);
        return "modals::addTag";
    }

    @GetMapping("/tags/{id}/update")
    public String getModalForUpdateTag(Model model, @PathVariable(value = "id") Long id) {
        Tag tag = tagDao.getTagById(id);

        Map<String, String> page = pageService.getPageDefaultParams();
        page.put("title", "update");
        model.addAttribute("tag", tag);
        model.addAttribute("page", page);
        return "modals::addTag";
    }

    @PutMapping("/tags/{id}/update")
    public String updateTag(@Valid Tag tag) {
        tagDao.updateTag(tag);
        return "modals::success";
    }

    @PostMapping("/tags/add")
    public String addTag(@Valid Tag tag) {
        tagDao.addTag(tag);
        return "modals::success";
    }

    @DeleteMapping("blog/tags/{id}")
    public String deleteTag(Model model, @PathVariable(value = "id") Long tagId) {
        tagDao.deleteTag(tagId);
        return "modals::success";
    }

    //TODO: refactor this when will be security. UserId must back from rest-api with every request.
    private ActiveUser getActiveUser() {
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        return user;
    }
}