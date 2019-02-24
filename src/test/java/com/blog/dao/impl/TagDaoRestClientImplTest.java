package com.blog.dao.impl;

import com.blog.model.Tag;
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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagDaoRestClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TagDaoRestClientImpl tagDaoRestClient;

    private static List<Tag> TAGS;

    @BeforeClass
    public static void setUp() {
        TAGS = new ArrayList<>();
        TAGS.add(new Tag());
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
}