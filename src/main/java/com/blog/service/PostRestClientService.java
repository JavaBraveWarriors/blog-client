package com.blog.service;

import com.blog.model.PostForAdd;
import com.blog.model.PostForGet;

import java.util.List;

public interface PostRestClientService {

    List<PostForGet> getListShortPosts(Long page, Long size);

    List<PostForGet> getListShortPostsWithSort(Long page, Long size, String sort);

    List<PostForGet> getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search);

    Long getCountPages(Long size);

    Long addPost(PostForAdd post);

    PostForGet getPostById(Long id);
}
