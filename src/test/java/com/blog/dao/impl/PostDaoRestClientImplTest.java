package com.blog.dao.impl;

import com.blog.config.SpringTestConfig;
import com.blog.model.Comment;
import com.blog.model.PostListWrapper;
import com.blog.model.RequestPostDto;
import com.blog.model.ResponsePostDto;
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
public class PostDaoRestClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PostDaoRestClientImpl postDaoRestClient;

    private static PostListWrapper COMMENTS_LIST_WRAPPER;
    private static List<ResponsePostDto> POSTS;
    private static ResponsePostDto POST_DTO;

    private static Long COUNT_PAGES = 10L;
    private static Long COUNT_POSTS = 100L;
    private static Long PAGE = 2L;
    private static Long SIZE = 10L;
    private static String SORT = "sortedField";
    private static String SEARCH = "testSearch";
    private static Long POST_ID_ADDED_POST = 12L;
    private static Long POST_ID = 1L;

    private static RequestPostDto ADDED_POST;

    @BeforeClass
    public static void setUp() {
        COMMENTS_LIST_WRAPPER = new PostListWrapper();

        ADDED_POST = new RequestPostDto();

        POSTS = new ArrayList<>();
        POSTS.add(new ResponsePostDto());

        POST_DTO = new ResponsePostDto();
        POST_DTO.setId(POST_ID);

        COMMENTS_LIST_WRAPPER.setCountPages(COUNT_PAGES);
        COMMENTS_LIST_WRAPPER.setCountPosts(COUNT_POSTS);
        COMMENTS_LIST_WRAPPER.setPosts(POSTS);
    }

    @Test
    public void getListShortPostsSuccess() {
        ResponseEntity<PostListWrapper> responseEntity = new ResponseEntity<>(COMMENTS_LIST_WRAPPER, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        PostListWrapper listShortPosts = postDaoRestClient.getListShortPosts(PAGE, SIZE);

        assertEquals(COUNT_POSTS, listShortPosts.getCountPosts());
        assertEquals(COUNT_PAGES, listShortPosts.getCountPages());
        assertEquals(COMMENTS_LIST_WRAPPER.getPosts().size(), listShortPosts.getPosts().size());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getListShortPostsWithIncorrectParams() {
        ResponseEntity<PostListWrapper> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));
        PostListWrapper listShortPosts = postDaoRestClient.getListShortPosts(PAGE, SIZE);
    }

    @Test
    public void getListShortPostsWithSortSuccess() {
        ResponseEntity<PostListWrapper> responseEntity = new ResponseEntity<>(COMMENTS_LIST_WRAPPER, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        PostListWrapper listShortPosts = postDaoRestClient.getListShortPostsWithSort(PAGE, SIZE, SORT);

        assertEquals(COUNT_POSTS, listShortPosts.getCountPosts());
        assertEquals(COUNT_PAGES, listShortPosts.getCountPages());
        assertEquals(COMMENTS_LIST_WRAPPER.getPosts().size(), listShortPosts.getPosts().size());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getListShortPostsWithSortWithIncorrectParams() {
        ResponseEntity<PostListWrapper> responseEntity = new ResponseEntity<>(COMMENTS_LIST_WRAPPER, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        PostListWrapper listShortPosts = postDaoRestClient.getListShortPostsWithSort(PAGE, SIZE, SORT);
    }

    @Test
    public void getListShortPostsWithSortAndSearchSuccess() {
        ResponseEntity<PostListWrapper> responseEntity = new ResponseEntity<>(COMMENTS_LIST_WRAPPER, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        PostListWrapper listShortPosts = postDaoRestClient.getListShortPostsWithSortAndSearch(PAGE, SIZE, SORT, SEARCH);

        assertEquals(COUNT_POSTS, listShortPosts.getCountPosts());
        assertEquals(COUNT_PAGES, listShortPosts.getCountPages());
        assertEquals(COMMENTS_LIST_WRAPPER.getPosts().size(), listShortPosts.getPosts().size());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getListShortPostsWithSortAndSearch() {
        ResponseEntity<PostListWrapper> responseEntity =
                new ResponseEntity<>(COMMENTS_LIST_WRAPPER, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        PostListWrapper listShortPosts = postDaoRestClient.getListShortPostsWithSortAndSearch(PAGE, SIZE, SORT, SEARCH);
    }

    @Test
    public void addPostSuccess() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(POST_ID_ADDED_POST, HttpStatus.CREATED);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        postDaoRestClient.addPost(ADDED_POST);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Comment.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addIncorrectPost() throws JsonProcessingException {
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        postDaoRestClient.addPost(ADDED_POST);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void addPostWIthNotExistTag() throws JsonProcessingException {
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        postDaoRestClient.addPost(ADDED_POST);
    }

    @Test
    public void getPostByIdSuccess() {
        ResponseEntity<ResponsePostDto> responseEntity = new ResponseEntity<>(POST_DTO, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        ResponsePostDto postDto = postDaoRestClient.getPostById(POST_ID);

        assertEquals(POST_ID, postDto.getId());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getPostByIncorrectId() {
        ResponseEntity<ResponsePostDto> responseEntity = new ResponseEntity<>(POST_DTO, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        ResponsePostDto postDto = postDaoRestClient.getPostById(POST_ID);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getPostByNotExistId() {
        ResponseEntity<ResponsePostDto> responseEntity = new ResponseEntity<>(POST_DTO, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class));

        ResponsePostDto postDto = postDaoRestClient.getPostById(POST_ID);
    }

    @Test
    public void updatePostSuccess() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        postDaoRestClient.updatePost(ADDED_POST);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Comment.class));
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updateNotExistPost() throws JsonProcessingException {
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        postDaoRestClient.updatePost(ADDED_POST);
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void updateIncorrectPost() throws JsonProcessingException {
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Comment.class))).thenReturn("json-object");

        postDaoRestClient.updatePost(ADDED_POST);
    }
}