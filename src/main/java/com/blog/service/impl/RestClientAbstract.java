package com.blog.service.impl;

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

    protected HttpHeaders headers;

    protected HttpEntity<String> entity;

    protected RestTemplate restTemplate;

    protected String createURLWithEndpoint(String URI) {
        return address + ":" + port + serverURI + URI;
    }
}
