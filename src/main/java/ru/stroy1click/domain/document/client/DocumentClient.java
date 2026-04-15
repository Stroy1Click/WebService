package ru.stroy1click.domain.document.client;

import ru.stroy1click.domain.document.dto.DocumentDto;

import java.util.List;

public interface DocumentClient {

    List<DocumentDto> getAllByUserId(Long userId, String jwt);
}
