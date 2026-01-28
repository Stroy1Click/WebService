package ru.stroy1click.web.order.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.order.client.OrderClient;
import ru.stroy1click.web.order.dto.OrderDto;

import java.util.List;

@Slf4j
@Service
public class OrderClientImpl implements OrderClient {

    private final RestClient restClient;

    public OrderClientImpl(@Value("${url.order}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public List<OrderDto> getByUserId(Long userId, String jwt) {
        log.info("getByUserId {}", userId);
        try {
            return this.restClient.get()
                    .uri("/user?userId={userId}", userId)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, ((request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    }))
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException e){
            log.info("get error ", e);
            throw new ServiceUnavailableException(e.getMessage());
        }
    }

    @Override
    public OrderDto get(Long id, String jwt) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(OrderDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public List<OrderDto> getAll(String jwt) {
        log.info("getAll");
        try {
            return this.restClient.get()
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public OrderDto create(OrderDto dto, String jwt) {
        log.info("create {}", dto);
        try {
            return this.restClient.post()
                    .header("Authorization", "Bearer " + jwt)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(OrderDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void update(Long id, OrderDto dto, String jwt) {
        log.info("update {}, {}", id, dto);
        try {
            this.restClient.patch()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + jwt)
                    .body(dto)
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
    public void delete(Long id, String jwt) {
        log.info("delete {}", id);
        try {
            this.restClient.delete()
                    .uri("/{id}", id)
                    .header("Authorization", "Bearer " + jwt)
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
