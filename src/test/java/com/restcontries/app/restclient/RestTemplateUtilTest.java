package com.restcontries.app.restclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.restcontries.app.configuration.AppProperties;

@ExtendWith(MockitoExtension.class)
class RestTemplateUtilTest {

    @Mock
    private AppProperties appProperties;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestTemplateUtil restTemplateUtil;

    private static final String BASE_URL = "http://example.com/";

    @BeforeEach
    void setUp() {
        when(appProperties.getClientBaseUrl()).thenReturn(BASE_URL);
    }

    @Test
    void doGetRequest_SuccessfulResponse_ReturnsResponseBody() {
        String path = "test";
        String fullUrl = BASE_URL + path;
        String expectedResponse = "response";

        when(restTemplate.getForEntity(fullUrl, String.class))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        String response = restTemplateUtil.doGetRequest(path, String.class);

        assertEquals(expectedResponse, response);
    }

    @Test
    void doGetRequest_NullResponse_ReturnsEmptyOptional() {
        String path = "test";
        String fullUrl = BASE_URL + path;

        when(restTemplate.getForEntity(fullUrl, String.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        String response = restTemplateUtil.doGetRequest(path, String.class);

        assertNull(response);
    }

    @Test
    void doGetRequest_RestClientException_ReturnsEmptyOptional() {
        String path = "test";
        String fullUrl = BASE_URL + path;

        when(restTemplate.getForEntity(fullUrl, String.class))
                .thenThrow(new RestClientException("Error"));

        String response = restTemplateUtil.doGetRequest(path, String.class);

        assertNull(response);
    }
}
