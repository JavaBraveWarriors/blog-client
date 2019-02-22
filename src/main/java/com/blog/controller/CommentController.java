package com.blog.controller;

import com.blog.dao.CommentDao;
import com.blog.model.ActiveUser;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.model.Pagination;
import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private CommentDao commentDao;
    private PageService pageService;

    @Autowired
    public CommentController(CommentDao commentDao, PageService pageService) {
        this.commentDao = commentDao;
        this.pageService = pageService;
    }

    @GetMapping("/post/{postId}")
    public String getListOfCommentsByPostId(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @PathVariable(value = "postId") Long postId) {
        CommentListWrapper commentListWrapper = commentDao.getListOfCommentsByPostId(page, size, postId);

        setActiveUserInModelAttribute(model);

        model.addAttribute("comment", new Comment());
        model.addAttribute("postId", postId);
        model.addAttribute("comments", commentListWrapper.getCommentsPage());
        model.addAttribute("countComments", commentListWrapper.getCountCommentsInPost());
        return "commentFragment";
    }

    @GetMapping("/post/{postId}/pagination")
    public String getPagination(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @PathVariable(value = "postId") Long postId) {
        Pagination pagination = pageService.getPagination(
                size,
                commentDao.getCountCommentPagesInPost(postId, size),
                page);

        model.addAttribute("pagination", pagination);

        return "pager::commentsNavBar";
    }

    @PostMapping("/post")
    public String addComment(@Valid Comment comment) {
        commentDao.addComment(comment);

        return "modals::success";
    }

    @DeleteMapping("/post/{postId}/{id}")
    public String deleteComment(
            Model model,
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "id") Long commentId) {
        commentDao.deleteComment(postId, commentId);

        model.addAttribute("message", "Комментарий успешно добавлен.");

        return "modals::success";
    }

    @GetMapping("/{id}")
    public String getFormForUpdate(
            Model model,
            @PathVariable(value = "id") Long commentId) {
        Comment comment = commentDao.getCommentById(commentId);

        model.addAttribute("comment", comment);

        return "modals::update";
    }

    @PutMapping("/post")
    public String updateComment(
            Model model, @Valid Comment comment) {
        commentDao.updateComment(comment);

        model.addAttribute("comment", comment);

        return "modals::success";
    }

    //TODO: refactor this when will be security. UserId must back from rest-api with every request.
    private void setActiveUserInModelAttribute(Model model) {
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        model.addAttribute("user", user);
    }
}
