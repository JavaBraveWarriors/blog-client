package com.blog.controller;

import com.blog.model.*;
import com.blog.service.PostRestClientService;
import com.blog.service.TagRestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blog/posts")
public class PostsController {

    private PostRestClientService postService;
    private TagRestClientService tagService;

    @Autowired
    public PostsController(PostRestClientService postService, TagRestClientService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("")
    public String posts(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) String sort,
            @CookieValue(value = "sort", required = false) String sortCookie,
            HttpServletResponse response
    ) {
        Pagination pagination = new Pagination();
        pagination.setTotalPages(postService.getCountPages(size));
        pagination.setSize(size);
        pagination.setCurrentPage(page);
        List<PostForGet> posts;

        // TODO: do refactoring.
        if (sortCookie != null && sort == null) {
            sort = sortCookie;
            posts = postService.getListShortPostsWithSort(page, size, sort);
        } else if (sortCookie != null && !sortCookie.isEmpty() && !sort.equals(sortCookie)) {
            response.addCookie(new Cookie("sort", sort));
            posts = postService.getListShortPostsWithSort(page, size, sort);
        } else if ((sortCookie == null || sortCookie.isEmpty()) && sort != null) {
            response.addCookie(new Cookie("sort", sort));
            posts = postService.getListShortPostsWithSort(page, size, sort);
        } else {
            posts = postService.getListShortPosts(page, size);
        }

        Map<String, String> currentPage = Page.getPageDafaultParams();
        currentPage.put("sort", sort);

        model.addAttribute("posts", posts);
        model.addAttribute("page", currentPage);
        model.addAttribute("pagination", pagination);
        return "blogPosts";
    }

    @GetMapping("/{id}")
    public String getPagePost(@PathVariable(name = "id") Long id, Model model) {
        PostForGet post = postService.getPostById(id);
        Map<String, String> currentPage = Page.getPageDafaultParams();

        model.addAttribute("post", post);
        model.addAttribute("page", currentPage);

        return "blogPost";
    }

    @GetMapping("/new")
    public String getPageForAddPost(Model model) {
        List<Tag> tags = tagService.getAllTags();

        Map<String, String> currentPage = Page.getPageDafaultParams();
        PostForAdd post = new PostForAdd();
        post.setTags(new ArrayList<>());
        model.addAttribute("post", post);

        model.addAttribute("allTags", tags);

        model.addAttribute("page", currentPage);

        return "blogAddPost";
    }

    @PostMapping("/new")
    public String addPost(@ModelAttribute PostForAdd post) {
        Long postId = postService.addPost(post);
        return "redirect:/blog/posts/" + postId;
    }
}
