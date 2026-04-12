package ru.stroy1click.domain.product.client;

import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.domain.common.client.CrudOperations;
import ru.stroy1click.domain.common.dto.PageResponse;
import ru.stroy1click.domain.product.dto.ProductDto;
import ru.stroy1click.domain.product.dto.ProductImageDto;

import java.util.List;

public interface ProductClient extends CrudOperations<ProductDto, Integer> {

    PageResponse<ProductDto> getByPagination(Integer page, Integer size, Integer categoryId,
                                             Integer subcategoryId, Integer productTypeId);

    List<ProductImageDto> getImages(Integer id);

    void assignImages(Integer id, List<MultipartFile> images, String jwt);

    void deleteImage(Integer id, String link, String jwt);
}
