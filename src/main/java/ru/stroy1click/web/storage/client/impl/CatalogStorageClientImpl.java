package ru.stroy1click.web.storage.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.storage.client.CatalogStorageClient;

@Slf4j
@Service
public class CatalogStorageClientImpl implements CatalogStorageClient {

    private final RestClient restClient;

    public CatalogStorageClientImpl(@Value("${url.catalogStorage}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public byte[] downloadImage(String link) {
        log.info("getPhoto {}", link);
        try {
            return this.restClient.get()
                    .uri("/{link}",link )
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        ValidationErrorUtils.validateStatus(response);
                    })
                    .body(byte[].class);
        } catch (ResourceAccessException e) {
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }

}
