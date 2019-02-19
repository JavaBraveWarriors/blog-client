package com.blog.service;

import com.blog.model.PostListWrapper;

import javax.servlet.http.HttpServletResponse;

public interface PostService {

    PostListWrapper routeRequestForListPosts(Long page, Long size, String sort, String search, String sortCookie, HttpServletResponse response);
}
