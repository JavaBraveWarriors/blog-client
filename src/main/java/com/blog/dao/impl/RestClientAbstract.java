package com.blog.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract client for rest-api.
 */
public abstract class RestClientAbstract {

    @Value("${rest.service.port}")
    protected String port;

    @Value("${rest.service.address}")
    protected String address;

    @Value("${rest.service.uri}")
    protected String serverURI;

    protected ObjectMapper jsonConverter;

    protected HttpHeaders headers;

    protected HttpEntity<String> entity;

    protected RestTemplate restTemplate;

    /**
     * Create url with endpoint and uri.
     *
     * @param URI is {String} value defines a specific server resource.
     * @return {String} value url where the request will be sent.
     */
    protected String createURLWithEndpoint(String URI) {
        return address + ":" + port + serverURI + URI;
    }

    /**
     * Convert object to json string.
     *
     * @param object is {Object} which is converted to json
     * @return {String} json representation of the object.
     */
    protected String convertToJson(Object object) {
        try {
            return jsonConverter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getEndpoint();
}
