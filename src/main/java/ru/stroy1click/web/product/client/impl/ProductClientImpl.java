package ru.stroy1click.web.product.client.impl;

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
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.dto.PageResponse;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.client.ProductClient;
import ru.stroy1click.web.product.dto.ProductDto;
import ru.stroy1click.web.product.dto.ProductImageDto;
import ru.stroy1click.web.security.TokenLifecycleInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@CircuitBreaker(name = "productClient")
public class ProductClientImpl implements ProductClient {

    private final RestClient restClient;

    public ProductClientImpl(@Value("${url.product}") String url,
                             TokenLifecycleInterceptor interceptor){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .requestInterceptors(clientHttpRequestInterceptors ->
                        clientHttpRequestInterceptors.addFirst(interceptor))
                .build();
    }

    @Override
    public PageResponse<ProductDto> getByPagination(Integer page, Integer size,
                                                    Integer categoryId, Integer subcategoryId,
                                                    Integer productTypeId) {
        log.info("getByPagination page - {}, size - {}", page, size);
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .queryParamIfPresent("categoryId", Optional.ofNullable(categoryId))
                            .queryParamIfPresent("subcategoryId", Optional.ofNullable(subcategoryId))
                            .queryParamIfPresent("productTypeId", Optional.ofNullable(productTypeId))
                            .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public List<ProductDto> getAll() {
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
    public List<ProductImageDto> getImages(Integer id) {
        log.info("getImages {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}/images",id)
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
    public void assignImages(Integer id, List<MultipartFile> images, String jwt) {
        log.info("assignImages {}", id);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        try {
            for (MultipartFile image : images) {
                builder.part("images", image.getBytes())
                        .filename(Objects.requireNonNull(image.getOriginalFilename()))
                        .contentType(MediaType.parseMediaType(
                                Objects.requireNonNull(image.getContentType())
                        ));
            }

            this.restClient.post()
                    .uri("/{id}/images", id)
                    .header("Authorization", "Bearer " + jwt)
                    .body(builder.build())
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

    @Override
    public ProductDto get(Integer id) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public ProductDto create(ProductDto dto, String jwt) {
        log.info("create {}", dto);
        try {
            return this.restClient.post()
                    .header("Authorization", "Bearer " + jwt)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(ProductDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void update(Integer id, ProductDto dto, String jwt) {
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
