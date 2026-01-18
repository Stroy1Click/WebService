package ru.stroy1click.web.common.client;

import org.springframework.web.multipart.MultipartFile;

public interface ImageManager<ID> {

    void assignImage(ID id, MultipartFile image, String jwt);

    void deleteImage(ID id, String link, String jwt);
}
