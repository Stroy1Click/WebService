package ru.stroy1click.web.user.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

@Slf4j
@Component
@CircuitBreaker(name = "userClient")
public class UserClientImpl implements UserClient {

    private final RestClient restClient;

    public UserClientImpl(@Value("${url.user}") String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public UserDto get(Long id) {
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
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
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,(request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(Void.class);
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
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,(request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(Void.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public UserDto getByEmail(String email) {
        log.info("getByEmail {}", email);
        try {
            return this.restClient.get()
                    .uri("/email?email={email}",email)
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
}
