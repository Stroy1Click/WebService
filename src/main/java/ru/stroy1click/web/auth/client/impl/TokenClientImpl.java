package ru.stroy1click.web.auth.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.auth.client.TokenClient;
import ru.stroy1click.web.auth.dto.JwtResponse;
import ru.stroy1click.web.auth.dto.RefreshTokenRequest;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;

@Slf4j
@Service
public class TokenClientImpl implements TokenClient {

    private final RestClient restClient;

    public TokenClientImpl(@Value("${url.token}") String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public JwtResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("registration");
        try {
            return this.restClient.post()
                    .uri("/access")
                    .body(refreshTokenRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(JwtResponse.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }
}
