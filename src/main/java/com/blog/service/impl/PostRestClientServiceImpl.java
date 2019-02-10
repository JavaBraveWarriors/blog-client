package com.blog.service.impl;

import com.blog.model.Post;
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

import java.util.List;

@Service
public class PostRestClientServiceImpl extends RestClientAbstract implements PostRestClientService {

    @Autowired
    public PostRestClientServiceImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.jsonConverter = jsonConverter;
        endpoint = "posts";
    }

    @Override
    public List<Post> getListShortPosts(Long page, Long size) {
        entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Post>> posts = restTemplate.exchange(
                createURLWithEndpoint(endpoint.concat("?page=").concat(page.toString()).concat("&size=").concat(size.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Post>>() {
                }
        );
        return posts.getBody();
    }

    @Override
    public List<Post> getListShortPostsWithSort(Long page, Long size, String sort) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<Post>> posts = restTemplate.exchange(
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
                new ParameterizedTypeReference<List<Post>>() {
                }
        );
        return posts.getBody();
    }

    @Override
    public List<Post> getListShortPostsWithSortAndSearch(Long page, Long size, String sort, String search) {
        // TODO:
        return getListShortPostsWithSort(page, size, sort);
    }

    @Override
    public Long getCountPages(Long size) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<Long> count = restTemplate.exchange(
                createURLWithEndpoint(endpoint.concat("/count?size=") + size),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Long>() {
                }
        );
        return count.getBody();
    }

    @Override
    public Long addPost(Post post) {
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
}
