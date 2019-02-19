package com.blog.dao.impl;

import com.blog.dao.TagDao;
import com.blog.model.Tag;
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

@Repository
public class TagDaoRestClientImpl extends RestClientAbstract implements TagDao {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public TagDaoRestClientImpl(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        endpoint = "tags";
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
}
