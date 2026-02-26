package ru.stroy1click.web.document.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.document.client.DocumentClient;
import ru.stroy1click.web.document.dto.DocumentDto;
import ru.stroy1click.web.security.TokenLifecycleInterceptor;

import java.util.List;

@Slf4j
@Service
public class DocumentClientImpl implements DocumentClient {

    private final RestClient restClient;

    public DocumentClientImpl(@Value("${url.document}") String url,
                           TokenLifecycleInterceptor interceptor){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .requestInterceptors(clientHttpRequestInterceptors ->
                        clientHttpRequestInterceptors.addFirst(interceptor))
                .build();
    }

    @Override
    public List<DocumentDto> getAllByUserId(Long userId, String jwt) {
        log.info("getAllByUserId {}", userId);
        try {
            return this.restClient.get()
                    .uri("?userId={userId}", userId)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, ((request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    }))
                    .body(new ParameterizedTypeReference<>() {

                    });
        } catch (ResourceAccessException e){
            log.info("get error ", e);
            throw new ServiceUnavailableException(e);
        }
    }
}
