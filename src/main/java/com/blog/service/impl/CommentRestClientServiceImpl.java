package com.blog.service.impl;

import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.service.CommentRestClientService;
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
public class CommentRestClientServiceImpl extends RestClientAbstract implements CommentRestClientService {

    private static String SLASH = "/";

    @Autowired
    public CommentRestClientServiceImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.jsonConverter = jsonConverter;
        endpoint = "comments";
    }

    public CommentListWrapper getListOfCommentsByPostId(Long page, Long size, Long postId) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<CommentListWrapper> comments = restTemplate.exchange(
                createURLWithEndpoint(
                        endpoint
                                .concat("?size=")
                                .concat(size.toString())
                                .concat("&page=")
                                .concat(page.toString())
                                .concat("&postId=")
                                .concat(postId.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<CommentListWrapper>() {
                }
        );
        return comments.getBody();
    }

    public Long getCountCommentPagesInPost(Long postId, Long size) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<Long> comments = restTemplate.exchange(
                createURLWithEndpoint(
                        endpoint.concat("/count")
                                .concat("?size=")
                                .concat(size.toString())
                                .concat("&postId=")
                                .concat(postId.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Long>() {
                }
        );
        return comments.getBody();
    }

    public void addComment(Comment comment) {
        entity = new HttpEntity<>(convertToJson(comment), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint("posts/comment"),
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void deleteComment(Long postId, Long commentId) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(
                        "posts/".concat(postId.toString())
                                .concat("/comment/")
                                .concat(commentId.toString())),
                HttpMethod.DELETE,
                entity,
                String.class
        );
    }

    public Comment getCommentById(Long commentId) {
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<Comment> commentResponseEntity = restTemplate.exchange(
                createURLWithEndpoint(
                        endpoint.concat(SLASH)
                                .concat(commentId.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Comment>() {
                }
        );
        return commentResponseEntity.getBody();
    }

    public void updateComment(Comment comment) {
        entity = new HttpEntity<>(convertToJson(comment), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(endpoint),
                HttpMethod.PUT,
                entity,
                String.class
        );
    }
}
