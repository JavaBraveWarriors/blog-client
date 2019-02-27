package com.blog.controller;

import com.blog.dao.CommentDao;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.model.Pagination;
import com.blog.service.PageService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.blog.controller.JsonConverter.convertToJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {
    private static CommentListWrapper COMMENT_LIST_WRAPPER;
    private static List<Comment> COMMENTS;
    private static Comment COMMENT;
    private static Long AUTHOR_ID = 1L;
    private static Long POST_ID = 1L;
    private static Long COUNT_PAGES = 1L;
    private static Pagination PAGINATION;
    private static Long COMMENT_ID = 2L;
    private static String LOCALE_MESSAGE;


    private MockMvc mockMvc;

    @Mock
    private CommentDao commentDao;

    @Mock
    private PageService pageService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CommentController commentController;

    @BeforeClass
    public static void setData() {
        COMMENT = new Comment("commentTest", AUTHOR_ID, POST_ID);

        COMMENTS = new ArrayList<>();
        COMMENTS.add(COMMENT);
        COMMENTS.add(new Comment("testComment", AUTHOR_ID, POST_ID));

        COMMENT_LIST_WRAPPER = new CommentListWrapper();
        COMMENT_LIST_WRAPPER.setCommentsPage(COMMENTS);
        COMMENT_LIST_WRAPPER.setCountPages(COUNT_PAGES);
        COMMENT_LIST_WRAPPER.setCountCommentsInPost((long) COMMENTS.size());

        PAGINATION = new Pagination();
        PAGINATION.setCurrentPage(1L);
        PAGINATION.setTotalPages(COUNT_PAGES);
        PAGINATION.setSize(COMMENT_LIST_WRAPPER.getCountPages());

        LOCALE_MESSAGE = "localeMessage";

    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void getListOfCommentsByPostId() throws Exception {
        given(commentDao.getListOfCommentsByPostId(anyLong(), anyLong(), anyLong())).willReturn(COMMENT_LIST_WRAPPER);

        mockMvc.perform(get("/comments/post/{postId}", POST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("commentFragment"))
                .andExpect(forwardedUrl("commentFragment"))
                .andExpect(model().attribute("comments", hasSize(COMMENTS.size())))
                .andExpect(model().attribute("postId", POST_ID))
                .andExpect(model().attribute("countComments", COMMENT_LIST_WRAPPER.getCountCommentsInPost()));

        verify(commentDao, times(1)).getListOfCommentsByPostId(anyLong(), anyLong(), anyLong());
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    public void getPagination() throws Exception {
        given(pageService.getPagination(anyLong(), anyLong(), anyLong())).willReturn(PAGINATION);

        mockMvc.perform(get("/comments/post/{postId}/pagination", POST_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("pager::commentsNavBar"))
                .andExpect(forwardedUrl("pager::commentsNavBar"))
                .andExpect(model().attribute("pagination", is(PAGINATION)));

        verify(pageService, times(1)).getPagination(anyLong(), anyLong(), anyLong());
        verifyNoMoreInteractions(pageService);
    }

    @Test
    public void addComment() throws Exception {
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);

        mockMvc.perform(post("/comments/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(COMMENT)))
                .andExpect(status().isOk())
                .andExpect(view().name("modals::success"))
                .andExpect(forwardedUrl("modals::success"));

        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verify(commentDao, times(1)).addComment(any(Comment.class));
        verifyNoMoreInteractions(commentDao, messageSource);
    }

    @Test
    public void deleteComment() throws Exception {
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn(LOCALE_MESSAGE);

        mockMvc.perform(delete("/comments/post/{postId}/{id}", POST_ID, COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("modals::success"))
                .andExpect(forwardedUrl("modals::success"));

        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verify(commentDao, times(1)).deleteComment(anyLong(), anyLong());
        verifyNoMoreInteractions(messageSource, commentDao);
    }

    @Test
    public void getFormForUpdate() throws Exception {
        given(commentDao.getCommentById(anyLong())).willReturn(COMMENT);

        mockMvc.perform(get("/comments/{id}", COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("modals::update"))
                .andExpect(forwardedUrl("modals::update"))
                .andExpect(model().attribute("comment", is(COMMENT)));

        verify(commentDao, times(1)).getCommentById(anyLong());
        verifyNoMoreInteractions(commentDao);
    }

    @Test
    public void updateComment() throws Exception {
        given(messageSource.getMessage(anyString(), any(), any(Locale.class))).willReturn("message");

        mockMvc.perform(put("/comments/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertToJson(COMMENT)))
                .andExpect(status().isOk())
                .andExpect(view().name("modals::success"))
                .andExpect(forwardedUrl("modals::success"));

        verify(messageSource, times(1)).getMessage(anyString(), any(), any(Locale.class));
        verify(commentDao, times(1)).updateComment(any(Comment.class));
        verifyNoMoreInteractions(commentDao);
    }
}