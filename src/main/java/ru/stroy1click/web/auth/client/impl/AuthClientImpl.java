package ru.stroy1click.web.auth.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.auth.client.AuthClient;
import ru.stroy1click.web.auth.dto.AuthRequest;
import ru.stroy1click.web.auth.dto.JwtResponse;
import ru.stroy1click.web.auth.dto.RefreshTokenRequest;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.user.dto.UserDto;

@Slf4j
@Service
@CircuitBreaker(name = "authClient")
public class AuthClientImpl implements AuthClient {

    private final RestClient restClient;

    public AuthClientImpl(@Value("${url.auth}") String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public void registration(UserDto user) {
        log.info("registration");
        try {
            this.restClient.post()
                    .uri("/registration")
                    .body(user)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(String.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public JwtResponse login(AuthRequest authRequest) {
        log.info("login");
        try {
            return this.restClient.post()
                    .uri("/login")
                    .body(authRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,(request, response) -> {
                        ValidationErrorUtils.validateAuthenticationStatus(response);
                    })
                    .body(JwtResponse.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        log.info("logout");
        try {
            this.restClient.post()
                    .uri("/logout")
                    .body(refreshTokenRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(Void.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }
}
