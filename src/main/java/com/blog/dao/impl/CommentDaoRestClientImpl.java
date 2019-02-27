package com.blog.dao.impl;

import com.blog.dao.CommentDao;
import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * This interface implementation {CommentDao} allows operations to easily manage via restTemplate with remote server.
 *
 * @author Aliaksandr Yeutushenka
 */
@Repository
public class CommentDaoRestClientImpl extends RestClientAbstract implements CommentDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private static String SLASH = "/";

    @Autowired
    public CommentDaoRestClientImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.jsonConverter = jsonConverter;
    }

    public CommentListWrapper getListOfCommentsByPostId(Long page, Long size, Long postId) {
        LOGGER.debug("Gets list of comments by page = [{}], size = [{}] and post id = [{}].", page, size, postId);
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<CommentListWrapper> comments = restTemplate.exchange(
                createURLWithEndpoint(
                        getEndpoint()
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
        LOGGER.debug("Gets count of pages comments by size = [{}] and post id = [{}].", size, postId);
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<Long> comments = restTemplate.exchange(
                createURLWithEndpoint(
                        getEndpoint().concat("/count")
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
        LOGGER.debug("Add new comment = [{}].", comment);
        entity = new HttpEntity<>(convertToJson(comment), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint("posts/comment"),
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void deleteComment(Long postId, Long commentId) {
        LOGGER.debug("Delete comment by comment id = [{}] and post id = [{}].", commentId, postId);
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
        LOGGER.debug("Get comment by comment id = [{}].", commentId);
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<Comment> commentResponseEntity = restTemplate.exchange(
                createURLWithEndpoint(
                        getEndpoint().concat(SLASH)
                                .concat(commentId.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Comment>() {
                }
        );
        return commentResponseEntity.getBody();
    }

    public void updateComment(Comment comment) {
        LOGGER.debug("Update comment = [{}].", comment);
        entity = new HttpEntity<>(convertToJson(comment), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(getEndpoint()),
                HttpMethod.PUT,
                entity,
                String.class
        );
    }

    protected String getEndpoint() {
        return "comments";
    }
}
