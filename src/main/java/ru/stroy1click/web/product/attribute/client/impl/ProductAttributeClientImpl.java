package ru.stroy1click.web.product.attribute.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.model.PageResponse;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.attribute.client.ProductAttributeClient;
import ru.stroy1click.web.product.attribute.dto.ProductAttributeDto;
import ru.stroy1click.web.product.attribute.model.ProductAttributeValueFilter;

@Slf4j
@Service
public class ProductAttributeClientImpl implements ProductAttributeClient {

    private final RestClient restClient;

    public ProductAttributeClientImpl(@Value("${url.productAttribute}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public PageResponse<ProductAttributeDto> getProductIdsByAttributes(Integer page, Integer size, ProductAttributeValueFilter filter) {
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
    public ProductAttributeDto get(Integer id) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductAttributeDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void create(ProductAttributeDto dto, String jwt) {
        log.info("create {}", dto);
        try {
            this.restClient.post()
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
    public void update(Integer id, ProductAttributeDto dto, String jwt) {
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
}
