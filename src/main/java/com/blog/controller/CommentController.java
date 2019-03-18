package com.blog.controller;

import com.blog.dao.CommentDao;
import com.blog.messaging.CommentJMSProducer;
import com.blog.model.ActiveUser;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.model.Pagination;
import com.blog.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.validation.Valid;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {

    private static final String SUCCESS_UPDATE_COMMENT = "modal.success.updateComment";
    private static final String SUCCESS_ADD_COMMENT = "modal.success.addComment";
    private static final String SUCCESS_DELETE_COMMENT = "modal.success.deleteComment";

    private CommentDao commentDao;
    private PageService pageService;
    private CommentJMSProducer commentJMSProducer;

    @Autowired
    public CommentController(
            MessageSource messageSource,
            CommentDao commentDao,
            PageService pageService,
            CommentJMSProducer commentJMSProducer) {
        super(messageSource);
        this.commentDao = commentDao;
        this.pageService = pageService;
        this.commentJMSProducer = commentJMSProducer;
    }

    @GetMapping("/post/{postId}")
    public String getListOfCommentsByPostId(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Long size,
            @PathVariable(value = "postId") Long postId) {
        CommentListWrapper commentListWrapper = commentDao.getListOfCommentsByPostId(page, size, postId);

        model.addAttribute("user", getActiveUser());
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
    public String addComment(@Valid Comment comment, Model model) {
        //commentDao.addComment(comment);
        try {
            commentJMSProducer.receiveCommentStatus(commentJMSProducer.sendComment(comment));
            model.addAttribute("message", getLocaleMessage(SUCCESS_ADD_COMMENT));
            return "modals::success";
        } catch (JMSException e) {
            model.addAttribute("message", getLocaleMessage(SUCCESS_ADD_COMMENT));
            return "modals::error";
        }
    }

    @DeleteMapping("/post/{postId}/{id}")
    public String deleteComment(
            Model model,
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "id") Long commentId) {
        commentDao.deleteComment(postId, commentId);

        model.addAttribute("message", getLocaleMessage(SUCCESS_DELETE_COMMENT));

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
        // TODO: added this message to properties.
        model.addAttribute("message", getLocaleMessage(SUCCESS_UPDATE_COMMENT));

        return "modals::success";
    }

    //TODO: refactor this when will be security. UserId must back from rest-api with every request.
    private ActiveUser getActiveUser() {
        ActiveUser user = new ActiveUser();
        user.setAuthorize(true);
        user.setId(1L);

        return user;
    }
}
