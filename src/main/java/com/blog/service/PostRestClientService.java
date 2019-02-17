package com.blog.service;

import com.blog.model.PostForAdd;
import com.blog.model.PostForGet;
import com.blog.model.PostListWrapper;

public interface PostRestClientService {

    PostListWrapper getListShortPosts(Long page, Long size);

    PostListWrapper getListShortPostsWithSort(Long page, Long size, String sort);

    PostListWrapper getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search);

    Long addPost(PostForAdd post);

    PostForGet getPostById(Long id);
}
