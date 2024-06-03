package com.restcontries.app.restclient;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.restcontries.app.configuration.AppProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class RestTemplateUtil {

    private AppProperties appProperties;
    private RestTemplate restTemplate;
    private static final int MAX_RETRY = 3;
    private final AtomicInteger retryCount = new AtomicInteger(0);

    public <T> T doGetRequest(String path,  Class<T> responseType) {
        String url = appProperties.getClientBaseUrl() + path;
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, responseType);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            log.error("Error {} ", e.getLocalizedMessage());
            if(e.getLocalizedMessage().contains("Operation timed out")) {
                log.info("Retrying the request");
                if(retryCount.get() < MAX_RETRY) {
                    retryCount.incrementAndGet();
                    return doGetRequest(path, responseType);
                }
            }
        }
        return null;

    }
}

