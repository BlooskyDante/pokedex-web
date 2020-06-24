package com.pokedex.rest.util.rest;

import org.springframework.http.HttpMethod;

public interface InvokeRestService {
    <T, R> R invokeRestService(final String endpoint, HttpMethod method, T request, Class<R> expectedResponse);
}
