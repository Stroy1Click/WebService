package ru.stroy1click.web.product.type.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.type.client.ProductTypeClient;
import ru.stroy1click.web.product.type.dto.ProductTypeDto;
import ru.stroy1click.web.security.TokenLifecycleInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@CircuitBreaker(name = "productTypeClient")
public class ProductTypeClientImpl implements ProductTypeClient {

    private final RestClient restClient;

    public ProductTypeClientImpl(@Value("${url.productType}") String url,
                                 TokenLifecycleInterceptor interceptor){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .requestInterceptors(clientHttpRequestInterceptors ->
                        clientHttpRequestInterceptors.addFirst(interceptor))
                .build();
    }

    @Override
    public ProductTypeDto get(Integer id) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductTypeDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public List<ProductTypeDto> getAll() {
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

    @Override
    public ProductTypeDto create(ProductTypeDto dto, String jwt) {
        log.info("create {}", dto);
        try {
            return this.restClient.post()
                    .header("Authorization", "Bearer " + jwt)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductTypeDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void update(Integer id, ProductTypeDto dto, String jwt) {
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
    public void assignImage(Integer id, MultipartFile image, String jwt) {
        log.info("assignImage {}", id);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        try {
            builder.part("image", image.getBytes())
                    .filename(Objects.requireNonNull(image.getOriginalFilename()))
                    .contentType(MediaType.parseMediaType(Objects.requireNonNull(image.getContentType())));

            this.restClient.post()
                    .uri("/{id}/image", id)
                    .header("Authorization", "Bearer " + jwt)                    .body(builder.build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) ->
                            ValidationErrorUtils.validateStatus(res)
                    )
                    .toBodilessEntity();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }


    @Override
    public void deleteImage(Integer id, String link, String jwt) {
        log.info("deleteImage {}", id);
        try {
            this.restClient.delete()
                    .uri("/{id}/image?link={link}", id, link)
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
