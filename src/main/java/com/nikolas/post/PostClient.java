package com.nikolas.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class PostClient {

    private static final Logger log = LoggerFactory.getLogger(PostClient.class);
    private final RestClient restClient;

    public PostClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .requestInterceptor((request, body, execution) -> {
                    logRequest( request, body);
                    var response = execution.execute(request, body);
                    logResponse(request, response);
                    return response;
                })
//                .requestInterceptor(clientLoggerRequestInterceptor)
                .build();
    }

    private void logRequest(HttpRequest request, byte[] body) {
        //Logging implementation
        log.info("Request: {}", request);
    }

    // we need to cache the body of the response

    private void logResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        // Logging implementation
        byte[] responseBody = response.getBody().readAllBytes();
        log.info("Response: {}", new String(responseBody, Charset.defaultCharset()));
    }

    public List<Post> findAll() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Post findById(Integer id) {
        return restClient.get()
                .uri("/posts/{id}",id)
                .retrieve()
                .toEntity(Post.class)
                .getBody();
    }
}
