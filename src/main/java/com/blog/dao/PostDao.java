package com.blog.dao;

import com.blog.model.PostListWrapper;
import com.blog.model.RequestPostDto;
import com.blog.model.ResponsePostDto;

/**
 * This interface defines various operations management for the Post object.
 *
 * @author Aliaksandr Yeutushenka
 */
public interface PostDao {

    /**
     * Gets a list of post objects from a specific item, a specific amount.
     *
     * @param page is {Long} value ID of the page from which you want to get objects.
     * @param size is {Long} value the number of required objects.
     * @return {PostListWrapper} is a list of posts.
     */
    PostListWrapper getListShortPosts(Long page, Long size);

    /**
     * Gets a list of post objects from a specific item, a specific amount and with sorting.
     *
     * @param page is {Long} value ID of the page from which you want to get objects.
     * @param size is {Long} value the number of required objects.
     * @param sort is {String} value the field by which will be sorted.
     * @return {PostListWrapper} is a list of posts.
     */
    PostListWrapper getListShortPostsWithSort(Long page, Long size, String sort);

    /**
     * Gets a list of post objects from a specific item, a specific amount, with sorting and search queries.
     *
     * @param page   is {Long} value ID of the page from which you want to get objects.
     * @param size   is {Long} value the number of required objects.
     * @param sort   is {String} value the field by which will be sorted.
     * @param search is {String} value the search query.
     * @return {PostListWrapper} is a list of posts.
     */
    PostListWrapper getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search);

    /**
     * Add new post.
     *
     * @param post {PostForAdd} to be added.
     * @return {Long} is the value that is the id of the new post.
     */
    Long addPost(RequestPostDto post);

    /**
     * Gets a {PostForGet} object where id is equal to argument parameter.
     *
     * @param id {Long} value the ID of the post you want to get.
     * @return {Post} is a object which has this ID.
     */
    ResponsePostDto getPostById(Long id);

    /**
     * Update post.
     *
     * @param post {PostForAdd} to be updated.
     */
    void updatePost(RequestPostDto post);
}
