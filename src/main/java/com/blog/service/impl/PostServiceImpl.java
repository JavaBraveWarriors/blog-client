package com.blog.service.impl;

import com.blog.dao.PostDao;
import com.blog.model.PostListWrapper;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class PostServiceImpl implements PostService {

    private PostDao postDao;

    @Autowired
    public PostServiceImpl(PostDao postDao) {
        this.postDao = postDao;
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
            return postDao.getListShortPostsWithSort(page, size, sort);
        } else if (sortCookie != null && !sortCookie.isEmpty() && !sort.equals(sortCookie)) {
            response.addCookie(new Cookie("sort", sort));
            return postDao.getListShortPostsWithSort(page, size, sort);
        } else if ((sortCookie == null || sortCookie.isEmpty()) && sort != null) {
            response.addCookie(new Cookie("sort", sort));
            return postDao.getListShortPostsWithSort(page, size, sort);
        } else {
            return postDao.getListShortPosts(page, size);
        }
    }
}
