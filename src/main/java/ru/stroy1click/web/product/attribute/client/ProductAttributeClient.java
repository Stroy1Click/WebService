package ru.stroy1click.web.product.attribute.client;

import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.common.model.PageResponse;
import ru.stroy1click.web.product.attribute.dto.ProductAttributeDto;
import ru.stroy1click.web.product.attribute.model.ProductAttributeValueFilter;

public interface ProductAttributeClient extends CrudOperations<ProductAttributeDto, Integer> {

    PageResponse<ProductAttributeDto> getProductIdsByAttributes(Integer page, Integer size, ProductAttributeValueFilter filter);
}
