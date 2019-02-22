package com.blog.controller;

import com.blog.dao.PostDao;
import com.blog.dao.TagDao;
import com.blog.model.*;
import com.blog.service.PageService;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog/posts")
public class PostsController {

    private PostService postService;
    private PostDao postDao;
    private TagDao tagService;
    private PageService pageService;

    @Autowired
    public PostsController(PostService postService,
                           TagDao tagService,
                           PostDao postDao,
                           PageService pageService) {
        this.postDao = postDao;
        this.postService = postService;
        this.tagService = tagService;
        this.pageService = pageService;
    }

    @GetMapping("")
    public String posts(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) String sort,
            @CookieValue(value = "sort", required = false) String sortCookie,
            HttpServletResponse response) {

        PostListWrapper posts = postService.routeRequestForListPosts(page, size, sort, search, sortCookie, response);

        Pagination pagination = pageService.getPagination(size, posts.getCountPages(), page);

        Map<String, String> currentPage = pageService.getPageDefaultParams();
        currentPage.put("sort", sort);

        setActiveUserInModelAttribute(model);

        model.addAttribute("posts", posts.getPosts());
        model.addAttribute("page", currentPage);
        model.addAttribute("pagination", pagination);

        return "blogPosts";
    }

    @GetMapping("/{id}")
    public String getPagePost(@PathVariable(name = "id") Long id, Model model) {

        ResponsePostDto post = postDao.getPostById(id);

        Map<String, String> currentPage = pageService.getPageDefaultParams();

        setActiveUserInModelAttribute(model);

        model.addAttribute("post", post);
        model.addAttribute("page", currentPage);

        return "blogPost";
    }

    @GetMapping("/new")
    public String getPageForAddPost(Model model) {
        List<Tag> tags = tagService.getAllTags();

        Map<String, String> currentPage = pageService.getPageDefaultParams();
        RequestPostDto post = new RequestPostDto();
        post.setTags(new ArrayList<>());

        currentPage.put("title", "add");

        setActiveUserInModelAttribute(model);

        model.addAttribute("post", post);
        model.addAttribute("title", "");
        model.addAttribute("allTags", tags);
        model.addAttribute("page", currentPage);
        return "formPost";
    }

    @PostMapping("/new")
    public String addPost(@ModelAttribute RequestPostDto post) {
        Long postId = postDao.addPost(post);
        return "redirect:/blog/posts/" + postId;
    }

    //TODO: refactor this when will be security. UserId must back from rest-api with every request.
    private void setActiveUserInModelAttribute(Model model) {
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        model.addAttribute("user", user);
    }
}
