package ru.stroy1click.domain.common.client;

import org.springframework.web.multipart.MultipartFile;

public interface ImageAssignmentService<ID> {

    void assignImage(ID id, MultipartFile image, String jwt);

    void deleteImage(ID id, String link, String jwt);
}
