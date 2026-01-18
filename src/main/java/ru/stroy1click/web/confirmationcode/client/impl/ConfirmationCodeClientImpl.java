package ru.stroy1click.web.confirmationcode.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.confirmationcode.client.ConfirmationCodeClient;
import ru.stroy1click.web.confirmationcode.model.CodeVerificationRequest;
import ru.stroy1click.web.confirmationcode.model.CreateConfirmationCodeRequest;
import ru.stroy1click.web.confirmationcode.model.UpdatePasswordRequest;

@Slf4j
@Service
@CircuitBreaker(name = "confirmationCodeClient")
public class ConfirmationCodeClientImpl implements ConfirmationCodeClient {

    private final RestClient restClient;

    public ConfirmationCodeClientImpl(@Value("${url.confirmationCode}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public void verifyEmail(CodeVerificationRequest verificationRequest) {
        log.info("verifyEmail {}", verificationRequest);
        try {
            this.restClient.post()
                    .uri("/email/verify")
                    .body(verificationRequest)
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
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        log.info("updatePassword {}", updatePasswordRequest.getCodeVerificationRequest());
        try {
            this.restClient.post()
                    .uri("/password-reset")
                    .body(updatePasswordRequest)
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
    public void recreate(CreateConfirmationCodeRequest confirmationCodeRequest) {
        log.info("recreate {}", confirmationCodeRequest);
        try {
            this.restClient.post()
                    .uri("/regeneration")
                    .body(confirmationCodeRequest)
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
    public void create(CreateConfirmationCodeRequest createConfirmationCodeRequest) {
        log.info("create {}", createConfirmationCodeRequest);
        try {
            this.restClient.post()
                    .body(createConfirmationCodeRequest)
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
}
