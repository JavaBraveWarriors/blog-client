package com.blog.dao.impl;

import com.blog.dao.TagDao;
import com.blog.model.Tag;
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
public class TagDaoRestClientImpl extends RestClientAbstract implements TagDao {

    @Autowired
    public TagDaoRestClientImpl(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        endpoint = "tags";
    }

    public List<Tag> getAllTags() {
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
