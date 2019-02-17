package com.blog.service;

import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;

public interface CommentRestClientService {

    CommentListWrapper getListOfCommentsByPostId(Long page, Long size, Long postId);

    Long getCountCommentPagesInPost(Long postId, Long size);

    void addComment(Comment comment);

    void deleteComment(Long postId, Long commentId);

    Comment getCommentById(Long commentId);

    void updateComment(Comment comment);
}
