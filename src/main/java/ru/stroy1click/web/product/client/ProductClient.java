package ru.stroy1click.web.product.client;

import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.common.dto.PageResponse;
import ru.stroy1click.web.product.dto.ProductDto;
import ru.stroy1click.web.product.dto.ProductImageDto;

import java.util.List;

public interface ProductClient extends CrudOperations<ProductDto, Integer> {

    PageResponse<ProductDto> getByPagination(Integer page, Integer size, Integer categoryId,
                                             Integer subcategoryId, Integer productTypeId);

    List<ProductImageDto> getImages(Integer id);

    void assignImages(Integer id, List<MultipartFile> images, String jwt);

    void deleteImage(Integer id, String link, String jwt);
}
