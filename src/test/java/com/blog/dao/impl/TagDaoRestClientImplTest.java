package com.blog.dao.impl;

import com.blog.model.Tag;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagDaoRestClientImplTest {

    private static Long CORRECT_TAG_ID = 2L;
    private static Long NOT_EXIST_TAG_ID = 12L;
    private static Long INCORRECT_TAG_ID = -22L;
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TagDaoRestClientImpl tagDaoRestClient;

    private static List<Tag> TAGS;
    private static Long TAG_ID_ADDED = 4L;
    private static Tag ADDED_TAG;
    private static Tag TAG;


    @BeforeClass
    public static void setUp() {
        TAGS = new ArrayList<>();
        TAGS.add(new Tag());
        ADDED_TAG = new Tag();
        ADDED_TAG.setTitle("testTitle");
        ADDED_TAG.setPathImage("");
        TAG = new Tag(2L, "someTestTitle", "");
    }

    @Test
    public void getAllTags() {
        ResponseEntity<List<Tag>> responseEntity = new ResponseEntity<>(TAGS, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        List<Tag> tagList = tagDaoRestClient.getAllTags();

        assertEquals(TAGS.size(), tagList.size());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(ParameterizedTypeReference.class));
    }

    @Test
    public void getTagByIdSuccess() {
        ResponseEntity<Tag> responseEntity = new ResponseEntity<>(TAG, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);

        Tag tag = tagDaoRestClient.getTagById(CORRECT_TAG_ID);

        assertEquals(CORRECT_TAG_ID, tag.getId());

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void getTagByIncorrectId() {
        ResponseEntity<Tag> responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));

        Tag tag = tagDaoRestClient.getTagById(INCORRECT_TAG_ID);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void getNotExistTagById() {
        ResponseEntity<Tag> responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));

        Tag tag = tagDaoRestClient.getTagById(NOT_EXIST_TAG_ID);
    }

    @Test
    public void addTagSuccess() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(TAG_ID_ADDED, HttpStatus.CREATED);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any(Tag.class))).thenReturn("json-object");

        tagDaoRestClient.addTag(ADDED_TAG);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Tag.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void addIncorrectTag() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(TAG_ID_ADDED, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Tag.class))).thenReturn("json-object");

        tagDaoRestClient.addTag(ADDED_TAG);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Tag.class));
    }

    @Test
    public void updateTag() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(TAG_ID_ADDED, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.writeValueAsString(any(Tag.class))).thenReturn("json-object");

        tagDaoRestClient.updateTag(ADDED_TAG);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(Tag.class));
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void updateIncorrectTag() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(TAG_ID_ADDED, HttpStatus.OK);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Tag.class))).thenReturn("json-object");

        tagDaoRestClient.updateTag(ADDED_TAG);
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void updateNotExistTag() throws JsonProcessingException {
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(TAG_ID_ADDED, HttpStatus.OK);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));
        when(objectMapper.writeValueAsString(any(Tag.class))).thenReturn("json-object");

        tagDaoRestClient.updateTag(ADDED_TAG);
    }

    @Test
    public void deleteTagSuccess() {
        ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class))).thenReturn(responseEntity);

        tagDaoRestClient.deleteTag(CORRECT_TAG_ID);

        verify(restTemplate, times(1))
                .exchange(anyString(),
                        any(HttpMethod.class),
                        any(HttpEntity.class),
                        any(Class.class));
    }

    @Test(expected = HttpClientErrorException.NotFound.class)
    public void deleteNotExistTag() {
        ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        doThrow(HttpClientErrorException.NotFound.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));

        tagDaoRestClient.deleteTag(NOT_EXIST_TAG_ID);
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void deleteIncorrectTag() {
        ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        doThrow(HttpClientErrorException.BadRequest.class).when(restTemplate).exchange(
                anyString(),
                any(HttpMethod.class),
                any(HttpEntity.class),
                any(Class.class));

        tagDaoRestClient.deleteTag(NOT_EXIST_TAG_ID);
    }
}