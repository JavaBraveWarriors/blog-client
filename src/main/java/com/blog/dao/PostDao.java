package com.blog.dao;

import com.blog.model.PostListWrapper;
import com.blog.model.RequestPostDto;
import com.blog.model.ResponsePostDto;

public interface PostDao {

    PostListWrapper getListShortPosts(Long page, Long size);

    PostListWrapper getListShortPostsWithSort(Long page, Long size, String sort);

    PostListWrapper getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search);

    Long addPost(RequestPostDto post);

    ResponsePostDto getPostById(Long id);

    void updatePost(RequestPostDto post);
}
