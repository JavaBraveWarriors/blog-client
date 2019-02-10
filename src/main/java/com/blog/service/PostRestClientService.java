package com.blog.service;

import com.blog.model.Post;

import java.util.List;

public interface PostRestClientService {

    List<Post> getListShortPosts(Long page, Long size);

    List<Post> getListShortPostsWithSort(Long page, Long size, String sort);

    List<Post> getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search);

    Long getCountPages(Long size);

    Long addPost(Post post);
}
