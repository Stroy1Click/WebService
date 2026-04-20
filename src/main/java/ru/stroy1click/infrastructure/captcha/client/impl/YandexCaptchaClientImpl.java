package ru.stroy1click.infrastructure.captcha.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.stroy1click.common.exception.ServiceUnavailableException;
import ru.stroy1click.infrastructure.captcha.client.YandexCaptchaClient;
import ru.stroy1click.infrastructure.captcha.dto.SmartCaptchaResponse;

import java.util.Map;

@Slf4j
@Component
public class YandexCaptchaClientImpl implements YandexCaptchaClient {

    private final RestClient restClient;

    @Value("${captcha.secret}")
    private String secret;

    public YandexCaptchaClientImpl(@Value("${url.yandex}") String url,
                                   RestClient.Builder restClient) {
        this.restClient = restClient
                .baseUrl(url)
                .build();
    }

    @Override
    public SmartCaptchaResponse sendCaptcha(String token) {
        log.debug("sendCaptcha {}", token);
        try {
            Map<String, String> params = Map.of(
                    "secret", secret,
                    "token", token
            );
            return this.restClient.post()
                    .uri(
                            uriBuilder -> uriBuilder.path("/validate")
                                    .queryParams(MultiValueMap.fromSingleValue(params)).build()
                    )
                    .retrieve()
                    .body(SmartCaptchaResponse.class);
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }
}
