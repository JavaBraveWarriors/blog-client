package com.blog.controller;

import com.blog.Post;
import com.blog.model.Page;
import com.blog.model.Pagination;
import com.blog.service.PostRestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private PostRestClientService postService;

    @Autowired
    public PostsController(PostRestClientService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public String posts(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) String sort,
            @CookieValue(value = "SORT", required = false) String sortCookie,
            HttpServletResponse response
    ) {
        Pagination pagination = new Pagination();
        pagination.setTotalPages(postService.getCountPages(size));
        pagination.setSize(size);
        pagination.setCurrentPage(page);
        List<Post> posts;

        // TODO: do refactoring.
        if (sortCookie != null && sort == null) {
            sort = sortCookie;
            posts = postService.getListShortPostsWithSort(page, size, sort);
        } else if (sortCookie != null && !sortCookie.isEmpty() && !sort.equals(sortCookie)) {
            response.addCookie(new Cookie("SORT", sort));
            posts = postService.getListShortPostsWithSort(page, size, sort);
        } else if ((sortCookie == null || sortCookie.isEmpty()) && sort != null) {
            response.addCookie(new Cookie("SORT", sort));
            posts = postService.getListShortPostsWithSort(page, size, sort);
        } else {
            posts = postService.getListShortPosts(page, size);
        }

        Page currentPage = new Page();
        currentPage.setSearch(true);
        currentPage.setSort(sort);
        currentPage.setTitle("Posts page");
        currentPage.setMenuItem("blog");
        model.addAttribute("posts", posts);
        model.addAttribute("page", currentPage);
        model.addAttribute("pagination", pagination);
        return "blogPosts";
    }

}
