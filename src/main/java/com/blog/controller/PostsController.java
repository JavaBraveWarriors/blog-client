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
            HttpServletResponse response) {

        PostListWrapper posts;

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
        Pagination pagination = new Pagination();
        pagination.setTotalPages(posts.getCountPages());
        pagination.setSize(size);
        pagination.setCurrentPage(page);

        Map<String, String> currentPage = Page.getPageDefaultParams();
        currentPage.put("sort", sort);
        //TODO: refactor this when will be security
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts.getPosts());
        model.addAttribute("page", currentPage);
        model.addAttribute("pagination", pagination);
        return "blogPosts";
    }

    @GetMapping("/{id}")
    public String getPagePost(@PathVariable(name = "id") Long id, Model model) {
        PostForGet post = postService.getPostById(id);
        Map<String, String> currentPage = Page.getPageDefaultParams();

        //TODO: refactor this when will be security
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("page", currentPage);

        return "blogPost";
    }

    @GetMapping("/new")
    public String getPageForAddPost(Model model) {
        List<Tag> tags = tagService.getAllTags();

        Map<String, String> currentPage = Page.getPageDefaultParams();
        PostForAdd post = new PostForAdd();
        post.setTags(new ArrayList<>());

        currentPage.put("title", "add");

        model.addAttribute("post", post);
        model.addAttribute("title", "");
        model.addAttribute("allTags", tags);
        model.addAttribute("page", currentPage);
        return "formPost";
    }

    @GetMapping("/{postId}/update")
    public String getPageForUpdatePost(
            Model model,
            @PathVariable(value = "postId") Long postId) {
        List<Tag> tags = tagService.getAllTags();

        PostForGet post = postService.getPostById(postId);
        Map<String, String> currentPage = Page.getPageDefaultParams();

        currentPage.put("title", "update");

        model.addAttribute("post", post);
        model.addAttribute("page", currentPage);
        model.addAttribute("title", "");
        model.addAttribute("allTags", tags);
        return "formPost";
    }

    @PostMapping("/new")
    public String addPost(@ModelAttribute PostForAdd post) {
        Long postId = postService.addPost(post);
        return "redirect:/blog/posts/" + postId;
    }
}
