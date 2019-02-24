package com.blog.dao.impl;

import com.blog.dao.TagDao;
import com.blog.model.Tag;
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

import java.util.List;

/**
 * This interface implementation {TagDao} allows operations to easily manage via restTemplate with remote server.
 *
 * @author Aliaksandr Yeutushenka
 */
@Repository
public class TagDaoRestClientImpl extends RestClientAbstract implements TagDao {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SLASH = "/";

    @Autowired
    public TagDaoRestClientImpl(RestTemplate restTemplate, HttpHeaders headers, ObjectMapper jsonConverter) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.jsonConverter = jsonConverter;
        endpoint = "tags";
    }

    public Tag getTagById(Long id) {
        LOGGER.debug("Get tag by id = [{}].", id);
        entity = new HttpEntity<>(null, headers);
        ResponseEntity<Tag> post = restTemplate.exchange(
                createURLWithEndpoint(endpoint.concat(SLASH).concat(id.toString())),
                HttpMethod.GET,
                entity,
                Tag.class
        );
        return post.getBody();
    }

    public List<Tag> getAllTags() {
        LOGGER.debug("Get full list of tags in the system.");

        entity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Tag>> tags = restTemplate.exchange(
                createURLWithEndpoint(endpoint),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Tag>>() {
                }
        );
        return tags.getBody();
    }

    public void addTag(Tag tag) {
        LOGGER.debug("Add new tag = [{}].", tag);

        entity = new HttpEntity<>(convertToJson(tag), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(endpoint),
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void updateTag(Tag tag) {
        LOGGER.debug("Update tag = [{}].", tag);

        entity = new HttpEntity<>(convertToJson(tag), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithEndpoint(endpoint),
                HttpMethod.PUT,
                entity,
                String.class
        );
    }
}
