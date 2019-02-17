package com.blog.service.impl;

import com.blog.model.PostForAdd;
import com.blog.model.PostForGet;
import com.blog.model.PostListWrapper;
import com.blog.service.PostRestClientService;
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
public class PostRestClientServiceImpl extends RestClientAbstract implements PostRestClientService {

    @Autowired
    public PostRestClientServiceImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
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

    public Long addPost(PostForAdd post) {
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

    public PostForGet getPostById(Long id) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<PostForGet> post = restTemplate.exchange(
                createURLWithEndpoint(endpoint.concat("/").concat(id.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PostForGet>() {
                }
        );
        return post.getBody();
    }
}
