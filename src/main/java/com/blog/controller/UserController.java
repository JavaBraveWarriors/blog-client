package com.blog.controller;

import com.blog.model.ActiveUser;
import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private PageService pageService;

    @Autowired
    public UserController(MessageSource messageSource, PageService pageService) {
        super(messageSource);
        this.pageService = pageService;
    }

    @GetMapping("/{id}")
    public String getUserPage(
            Model model,
            @PathVariable(name = "id") Long authorId) {
        return "";
    }

    @GetMapping("")
    public String getTableWithUsers(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) String sort,
            @CookieValue(value = "sort", required = false) String sortCookie,
            HttpServletResponse response) {
        return "";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        Map<String, String> currentPage = pageService.getPageDefaultParams();

        model.addAttribute("page", currentPage);
        model.addAttribute("user", getActiveUser());
        return "register";
    }

    @GetMapping("/login")
    public String getSinginPage(Model model) {
        return "modals::modelSingIn";
    }

    //TODO: refactor this when will be security. UserId must back from rest-api with every request.
    private ActiveUser getActiveUser() {
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);
        return user;
    }
}
