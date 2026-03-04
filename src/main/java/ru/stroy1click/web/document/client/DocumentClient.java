package ru.stroy1click.web.document.client;

import ru.stroy1click.web.document.dto.DocumentDto;

import java.util.List;

public interface DocumentClient {

    List<DocumentDto> getAllByUserId(Long userId, String jwt);
}
