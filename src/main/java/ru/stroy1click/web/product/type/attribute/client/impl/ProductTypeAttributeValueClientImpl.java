package ru.stroy1click.web.product.type.attribute.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.web.product.type.attribute.client.ProductTypeAttributeValueClient;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.type.attribute.dto.ProductTypeAttributeValueDto;

import java.util.List;

@Slf4j
@Service
public class ProductTypeAttributeValueClientImpl implements ProductTypeAttributeValueClient {

    private final RestClient restClient;

    public ProductTypeAttributeValueClientImpl(@Value("${url.productTypeAttributeValue}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public ProductTypeAttributeValueDto get(Integer id) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductTypeAttributeValueDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void create(ProductTypeAttributeValueDto dto, String jwt) {
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
    public void update(Integer id, ProductTypeAttributeValueDto dto, String jwt) {
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
    public List<ProductTypeAttributeValueDto> getProductTypeAttributeValuesByProductTypeId(Integer productTypeId) {
        log.info("getProductTypeAttributeValuesByProductTypeId {}", productTypeId);
        try {
            return this.restClient.get()
                    .uri("?productTypeId={productTypeId}", productTypeId)
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
