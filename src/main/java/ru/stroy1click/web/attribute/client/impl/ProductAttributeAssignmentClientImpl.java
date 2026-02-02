package ru.stroy1click.web.attribute.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.dto.PageResponse;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.attribute.client.ProductAttributeAssignmentClient;
import ru.stroy1click.web.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.web.attribute.dto.ProductAttributeValueFilter;

import java.util.List;

@Slf4j
@Service
@CircuitBreaker(name = "productAttributeAssignmentClient")
public class ProductAttributeAssignmentClientImpl implements ProductAttributeAssignmentClient {

    private final RestClient restClient;

    public ProductAttributeAssignmentClientImpl(@Value("${url.productAttributeAssignment}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(Integer page, Integer size, ProductAttributeValueFilter filter) {
        log.info("getByPagination page - {}, size - {}", page, size);
        try {
            return restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/filter")
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build()
                    )
                    .body(filter)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public ProductAttributeAssignmentDto get(Integer id) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductAttributeAssignmentDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public ProductAttributeAssignmentDto create(ProductAttributeAssignmentDto dto, String jwt) {
        log.info("create {}", dto);
        try {
            return this.restClient.post()
                    .header("Authorization", "Bearer " + jwt)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductAttributeAssignmentDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void update(Integer id, ProductAttributeAssignmentDto dto, String jwt) {
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
    public void delete(Integer id, String jwt) {
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

    @Override
    public List<ProductAttributeAssignmentDto> getAll() {
        log.info("getAll");
        try {
            return this.restClient.get()
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
}
