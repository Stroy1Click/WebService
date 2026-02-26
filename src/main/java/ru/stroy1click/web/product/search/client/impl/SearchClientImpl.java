package ru.stroy1click.web.product.search.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.search.client.SearchClient;

import java.util.List;

@Slf4j
@Service
@CircuitBreaker(name = "searchClient")
public class SearchClientImpl implements SearchClient {

    private final RestClient restClient;

    public SearchClientImpl(@Value("${url.search}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public List<Integer> getProductIds(String query) {
        log.info("getProductIds {}", query);
        try {
            return this.restClient.get()
                    .uri("?q={query}", query)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(new ParameterizedTypeReference<>() {}); // <-- важное исправление

        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }
}
