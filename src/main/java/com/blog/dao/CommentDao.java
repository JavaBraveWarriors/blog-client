package com.blog.dao;

import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;

/**
 * This interface defines various operations management for the Comment object.
 *
 * @author Aliaksandr Yeutushenka
 */
public interface CommentDao {

    /**
     * Gets a list of comments objects from a specific item, a specific amount by postId.
     *
     * @param page   is {Long} value ID of the page from which you want to get objects.
     * @param size   is {Long} value the number of required objects.
     * @param postId is {Long} value the post where comments are taken.
     * @return the list comments by post id with pagination.
     */
    CommentListWrapper getListOfCommentsByPostId(Long page, Long size, Long postId);

    /**
     * Gets count comment pages in post.
     *
     * @param postId is {Long} value the post where comments are taken.
     * @param size   is {Long} value the number size of one page.
     * @return the count comment pages in post.
     */
    Long getCountCommentPagesInPost(Long postId, Long size);

    /**
     * Add new comment.
     *
     * @param comment {Comment} to be added.
     */
    void addComment(Comment comment);

    /**
     * Delete comment.
     *
     * @param postId    is {Long} value which identifies the post ID.
     * @param commentId is {Long} value which identifies the comment ID.
     */
    void deleteComment(Long postId, Long commentId);

    /**
     * Gets a {Comment} object where id is equal to argument parameter
     *
     * @param commentId {Long} value the ID of the comment you want to get
     * @return {Comment} is a object which has this ID.
     */
    Comment getCommentById(Long commentId);

    /**
     * Update comment.
     *
     * @param comment {Comment} to be updated.
     */
    void updateComment(Comment comment);
}
