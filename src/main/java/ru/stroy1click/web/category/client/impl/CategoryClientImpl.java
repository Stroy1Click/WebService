package ru.stroy1click.web.category.client.impl;

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
import ru.stroy1click.web.category.client.CategoryClient;
import ru.stroy1click.web.category.dto.CategoryDto;
import ru.stroy1click.web.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@CircuitBreaker(name = "categoryClient")
public class CategoryClientImpl implements CategoryClient {

    private final RestClient restClient;

    public CategoryClientImpl(@Value("${url.category}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public CategoryDto get(Integer id) {
        log.info("get {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(CategoryDto.class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

    @Override
    public void create(CategoryDto dto, String jwt) {
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
    public void update(Integer id, CategoryDto dto, String jwt) {
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
    public List<CategoryDto> getAll() {
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
    public List<SubcategoryDto> getSubcategories(Integer id) {
        log.info("getSubcategories {}", id);
        try {
            return this.restClient.get()
                    .uri("/{id}/subcategories",id)
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
