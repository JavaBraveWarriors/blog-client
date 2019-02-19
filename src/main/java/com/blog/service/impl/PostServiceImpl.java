package com.blog.service.impl;

import com.blog.model.PostListWrapper;
import com.blog.service.PostRestClientService;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class PostServiceImpl implements PostService {

    private PostRestClientService postService;

    @Autowired
    public PostServiceImpl(PostRestClientService postService) {
        this.postService = postService;
    }

    @Override
    public PostListWrapper routeRequestForListPosts(
            Long page,
            Long size,
            String sort,
            String search,
            String sortCookie,
            HttpServletResponse response) {
        if (sortCookie != null && sort == null) {
            sort = sortCookie;
            return postService.getListShortPostsWithSort(page, size, sort);
        } else if (sortCookie != null && !sortCookie.isEmpty() && !sort.equals(sortCookie)) {
            response.addCookie(new Cookie("sort", sort));
            return postService.getListShortPostsWithSort(page, size, sort);
        } else if ((sortCookie == null || sortCookie.isEmpty()) && sort != null) {
            response.addCookie(new Cookie("sort", sort));
            return postService.getListShortPostsWithSort(page, size, sort);
        } else {
            return postService.getListShortPosts(page, size);
        }
    }
}
