package com.blog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class RestClientAbstract {

    @Value("${rest.service.port}")
    protected String port;

    @Value("${rest.service.address}")
    protected String address;

    @Value("${rest.service.uri}")
    protected String serverURI;

    protected String endpoint;

    protected ObjectMapper jsonConverter;

    protected HttpHeaders headers;

    protected HttpEntity<String> entity;

    protected RestTemplate restTemplate;

    protected String createURLWithEndpoint(String URI) {
        return address + ":" + port + serverURI + URI;
    }

    protected String convertToJson(Object object) {
        try {
            return jsonConverter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
