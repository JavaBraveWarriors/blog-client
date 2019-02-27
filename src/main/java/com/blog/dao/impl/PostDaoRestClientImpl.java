package com.blog.dao.impl;

import com.blog.dao.PostDao;
import com.blog.model.PostListWrapper;
import com.blog.model.RequestPostDto;
import com.blog.model.ResponsePostDto;
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
 * This interface implementation {PostDao} allows operations to easily manage via restTemplate with remote server.
 *
 * @author Aliaksandr Yeutushenka
 */
@Repository
public class PostDaoRestClientImpl extends RestClientAbstract implements PostDao {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public PostDaoRestClientImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.jsonConverter = jsonConverter;
    }

    public PostListWrapper getListShortPosts(Long page, Long size) {
        LOGGER.debug("Gets list of posts by page = [{}], size = [{}].", page, size);

        entity = new HttpEntity<>(null, headers);

        ResponseEntity<PostListWrapper> posts = restTemplate.exchange(
                createURLWithEndpoint(getEndpoint().concat("?page=").concat(page.toString()).concat("&size=").concat(size.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<PostListWrapper>() {
                }
        );
        return posts.getBody();
    }

    public PostListWrapper getListShortPostsWithSort(Long page, Long size, String sort) {
        LOGGER.debug("Gets list of posts by page = [{}], size = [{}] and sort = [{}].", page, size, sort);

        entity = new HttpEntity<>(null, headers);
        ResponseEntity<PostListWrapper> posts = restTemplate.exchange(
                createURLWithEndpoint(
                        getEndpoint()
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
        LOGGER.debug("Gets list of posts by page = [{}], size = [{}], sort = [{}] and search = [{}].", page, size, sort, search);
        // TODO:
        return getListShortPostsWithSort(page, size, sort);
    }

    public Long addPost(RequestPostDto post) {
        LOGGER.debug("Add new post = [{}].", post);
        entity = new HttpEntity<>(convertToJson(post), headers);
        ResponseEntity<Long> postId = restTemplate.exchange(
                createURLWithEndpoint(getEndpoint()),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Long>() {
                }
        );
        return postId.getBody();
    }

    public ResponsePostDto getPostById(Long id) {
        LOGGER.debug("Get post by post id = [{}].", id);
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<ResponsePostDto> post = restTemplate.exchange(
                createURLWithEndpoint(getEndpoint().concat("/").concat(id.toString())),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<ResponsePostDto>() {
                }
        );
        return post.getBody();
    }

    public void updatePost(RequestPostDto post) {
        LOGGER.debug("Update post = [{}].", post);
        entity = new HttpEntity<>(convertToJson(post), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(getEndpoint()),
                HttpMethod.PUT,
                entity,
                String.class
        );
    }

    protected String getEndpoint() {
        return "posts";
    }
}
