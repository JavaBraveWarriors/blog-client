package com.blog.dao.impl;


import com.blog.config.SpringTestConfig;
import com.blog.dao.impl.CommentDaoRestClientImpl;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class)
public class CommentDaoRestClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CommentDaoRestClientImpl commentDao;

    private static CommentListWrapper COMMENTS_LIST_WRAPPER;
    private static List<Comment> COMMENTS;

    private static Long COMMENT_ID_ADDED_COMMENT = 22L;
    private static Long COUNT_PAGES = 10L;
    private static Long COUNT_COMMENTS_IN_POST = 20L;
    private static Long PAGE = 2L;
    private static Long SIZE = 10L;
    private static Long POST_ID = 20L;
    private static Long INCORRECT_POST_ID = -2L;
    private static Long CORRECT_AUTHOR_ID = 12L;
    private static Long CORRECT_COMMENT_ID = 122L;
    private static Long INCORRECT_COMMENT_ID = -22L;
    private static Long NOT_EXIST_COMMENT_ID = 11122L;

    private static Comment ADDED_COMMENT = new Comment("some text", CORRECT_AUTHOR_ID, POST_ID);
    private static Comment COMMENT = new Comment("testText", CORRECT_AUTHOR_ID, POST_ID);

    @BeforeClass
    public static void setUp() {
        COMMENTS_LIST_WRAPPER = new CommentListWrapper();
        COMMENTS = new ArrayList<>();
        COMMENTS.add(new Comment());
        COMMENTS.add(new Comment());
        COMMENTS_LIST_WRAPPER.setCommentsPage(COMMENTS);
        COMMENTS_LIST_WRAPPER.setCountCommentsInPost(COUNT_COMMENTS_IN_POST);
        COMMENTS_LIST_WRAPPER.setCountPages(COUNT_PAGES);
    }

    @Test
    public void getListOfCommentsByPostIdSuccess() {
        ResponseEntity<CommentListWrapper> responseEntity = new ResponseEntity<>(COMMENTS_LIST_WRAPPER, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        CommentListWrapper listOfCommentsByPostId = commentDao.getListOfCommentsByPostId(PAGE, SIZE, POST_ID);

        assertEquals(COUNT_COMMENTS_IN_POST, listOfCommentsByPostId.getCountCommentsInPost());
        assertEquals(COUNT_PAGES, listOfCommentsByPostId.getCountPages());
        assertEquals(COMMENTS_LIST_WRAPPER.getCommentsPage().size(), listOfCommentsByPostId.getCommentsPage().size());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getListOfCommentsByIncorrectPostId() {
        ResponseEntity<CommentListWrapper> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        CommentListWrapper listOfCommentsByPostId = commentDao.getListOfCommentsByPostId(PAGE, SIZE, POST_ID);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getListOfCommentsByNotExistPostId() {
        ResponseEntity<CommentListWrapper> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        commentDao.getListOfCommentsByPostId(PAGE, SIZE, POST_ID);
    }

    @Test
    public void getCountCommentPagesInPostSuccess() {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(COUNT_COMMENTS_IN_POST, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        Long countPages = commentDao.getCountCommentPagesInPost(POST_ID, SIZE);

        assertEquals(COUNT_COMMENTS_IN_POST, countPages);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getCountCommentPagesByIncorrectPostId() {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        Long countPages = commentDao.getCountCommentPagesInPost(POST_ID, SIZE);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getCountCommentPagesByNotExistPostId() {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        Long countPages = commentDao.getCountCommentPagesInPost(POST_ID, SIZE);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test
    public void addCommentSuccess() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(COMMENT_ID_ADDED_COMMENT, HttpStatus.CREATED);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        commentDao.addComment(ADDED_COMMENT);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Comment.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addIncorrectComment() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        commentDao.addComment(ADDED_COMMENT);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void addCommentWithNotExistAuthorIdOrPostId() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        commentDao.addComment(ADDED_COMMENT);
    }

    @Test
    public void deleteCommentSuccess() {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);

        commentDao.deleteComment(POST_ID, CORRECT_COMMENT_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void deleteCommentWithIncorrectCommentId() {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        commentDao.deleteComment(INCORRECT_POST_ID, INCORRECT_COMMENT_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void deleteNotExistComment() {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        commentDao.deleteComment(POST_ID, NOT_EXIST_COMMENT_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
    }

    @Test
    public void getCommentByIdSuccess() {
        ResponseEntity<Comment> responseEntity = new ResponseEntity<>(COMMENT, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        commentDao.getCommentById(CORRECT_COMMENT_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getCommentByIncorrectId() {
        ResponseEntity<Comment> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        commentDao.getCommentById(INCORRECT_COMMENT_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getNotExistCommentById() {
        ResponseEntity<Comment> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        commentDao.getCommentById(INCORRECT_COMMENT_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test
    public void updateCommentSuccess() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        commentDao.updateComment(ADDED_COMMENT);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Comment.class));
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updateNotExistComment() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        commentDao.updateComment(ADDED_COMMENT);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Comment.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void updateCommentWithIncorrectFields() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        commentDao.updateComment(ADDED_COMMENT);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Comment.class));
    }

}