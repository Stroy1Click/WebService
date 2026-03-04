package ru.stroy1click.web.user.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.security.TokenLifecycleInterceptor;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

@Slf4j
@Component
@CircuitBreaker(name = "userClient")
public class UserClientImpl implements UserClient {

    private final RestClient restClient;

    public UserClientImpl(@Value("${url.user}") String url,
                          TokenLifecycleInterceptor interceptor) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .requestInterceptors(clientHttpRequestInterceptors ->
                        clientHttpRequestInterceptors.addFirst(interceptor))
                .build();
    }

    @Override
    public UserDto get(Long id, String jwt) {
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,(request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(UserDto.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void update(Long id, UserDto dto, String jwt) {
        log.info("update {}, {}", id, dto);
        try {
            this.restClient.patch()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + jwt)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,(request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(String.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void delete(Long id, String jwt) {
        log.info("delete {}", id);
        try {
            this.restClient.delete()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,(request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(String.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public UserDto getByEmail(String email, String jwt) {
        log.info("getByEmail {}", email);
        try {
            return this.restClient.get()
                    .uri("?email={email}" ,email)
                    .header("Authorization", "Bearer " + jwt)                    .retrieve()
                    .onStatus(HttpStatusCode::isError,(request, response) -> {
                        ValidationErrorUtils.validateAuthenticationStatus(response);
                    })
                    .body(UserDto.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }
}
