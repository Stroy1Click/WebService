package ru.stroy1click.web.storage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.web.storage.client.CatalogStorageClient;
import ru.stroy1click.web.storage.client.DocumentStorageClient;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {

    private final CatalogStorageClient catalogStorageClient;

    private final DocumentStorageClient documentStorageClient;

    @GetMapping("/catalog/{fileName}")
    public ResponseEntity<?> downloadCatalogImage(@PathVariable String fileName) {
        byte[] data = this.catalogStorageClient.downloadImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(data);
    }

    @GetMapping("/documents/{fileName}")
    public ResponseEntity<?> downloadDocument(@PathVariable String fileName) {
        byte[] data = this.documentStorageClient.downloadDocument(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(data);
    }
}
