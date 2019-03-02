package com.blog.controller;

import com.blog.dao.TagDao;
import com.blog.messaging.Producer;
import com.blog.model.ActiveUser;
import com.blog.model.Tag;
import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog")
public class TagController extends BaseController {

    private static final String SUCCESS_UPDATE_TAG = "modal.success.updateTag";
    private static final String SUCCESS_DELETE_TAG = "modal.success.deleteTag";
    private static final String SUCCESS_ADD_TAG = "modal.success.addTag";

    private TagDao tagDao;
    private PageService pageService;
    private Producer messageListener;

    @Autowired
    public TagController(PageService pageService, TagDao tagDao, MessageSource messageSource, Producer messageListener) {
        super(messageSource);
        this.tagDao = tagDao;
        this.pageService = pageService;
        this.messageListener = messageListener;
    }

    @GetMapping("")
    public String getPageWithTags(Model model) throws JMSException {
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
    public String updateTag(@Valid Tag tag, Model model) {
        tagDao.updateTag(tag);

        model.addAttribute("message",
                getLocaleMessage(SUCCESS_UPDATE_TAG));

        return "modals::success";
    }

    @PostMapping("/tags/add")
    public String addTag(@Valid Tag tag, Model model) {
        tagDao.addTag(tag);
        model.addAttribute("message",
                getLocaleMessage(SUCCESS_ADD_TAG));
        return "modals::success";
    }

    @DeleteMapping("/tags/{id}")
    public String deleteTag(Model model, @PathVariable(value = "id") Long tagId) {
        tagDao.deleteTag(tagId);
        model.addAttribute("message",
                getLocaleMessage(SUCCESS_DELETE_TAG));
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