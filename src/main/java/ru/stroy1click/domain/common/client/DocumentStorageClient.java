package ru.stroy1click.domain.common.client;

public interface DocumentStorageClient {

    byte[] downloadDocument(String link);
}
