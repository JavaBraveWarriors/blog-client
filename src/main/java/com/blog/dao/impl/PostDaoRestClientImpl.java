package com.blog.dao.impl;

import com.blog.dao.PostDao;
import com.blog.model.PostListWrapper;
import com.blog.model.RequestPostDto;
import com.blog.model.ResponsePostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostDaoRestClientImpl extends RestClientAbstract implements PostDao {

    @Autowired
    public PostDaoRestClientImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.jsonConverter = jsonConverter;
        endpoint = "posts";
    }

    public PostListWrapper getListShortPosts(Long page, Long size) {
        entity = new HttpEntity<>(null, headers);

        ResponseEntity<PostListWrapper> posts = restTemplate.exchange(
                createURLWithEndpoint(endpoint.concat("?page=").concat(page.toString()).concat("&size=").concat(size.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PostListWrapper>() {
                }
        );
        return posts.getBody();
    }

    public PostListWrapper getListShortPostsWithSort(Long page, Long size, String sort) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<PostListWrapper> posts = restTemplate.exchange(
                createURLWithEndpoint(
                        endpoint
                                .concat("?page=")
                                .concat(page.toString())
                                .concat("&size=")
                                .concat(size.toString())
                                .concat("&sort=")
                                .concat(sort)),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PostListWrapper>() {
                }
        );
        return posts.getBody();
    }

    public PostListWrapper getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search) {
        // TODO:
        return getListShortPostsWithSort(page, size, sort);
    }

    public Long addPost(RequestPostDto post) {
        entity = new HttpEntity<>(convertToJson(post), headers);
        ResponseEntity<Long> postId = restTemplate.exchange(
                createURLWithEndpoint(endpoint),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Long>() {
                }
        );
        return postId.getBody();
    }

    public ResponsePostDto getPostById(Long id) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResponsePostDto> post = restTemplate.exchange(
                createURLWithEndpoint(endpoint.concat("/").concat(id.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<ResponsePostDto>() {
                }
        );
        return post.getBody();
    }

    public void updatePost(RequestPostDto post) {
        entity = new HttpEntity<>(convertToJson(post), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(endpoint),
                HttpMethod.PUT,
                entity,
                String.class
        );
    }
}
