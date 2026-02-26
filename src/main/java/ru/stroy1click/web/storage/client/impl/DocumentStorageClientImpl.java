package ru.stroy1click.web.storage.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.common.exception.ServiceUnavailableException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.storage.client.DocumentStorageClient;

@Slf4j
@Service
public class DocumentStorageClientImpl implements DocumentStorageClient {

    private final RestClient restClient;

    public DocumentStorageClientImpl(@Value("${url.documentStorage}") String url){
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public byte[] downloadDocument(String link) {
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
