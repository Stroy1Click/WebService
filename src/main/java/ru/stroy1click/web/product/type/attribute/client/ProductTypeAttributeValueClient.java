package ru.stroy1click.web.product.type.attribute.client;

import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.product.type.attribute.dto.ProductTypeAttributeValueDto;

import java.util.List;

public interface ProductTypeAttributeValueClient
        extends CrudOperations<ProductTypeAttributeValueDto, Integer> {

    List<ProductTypeAttributeValueDto> getProductTypeAttributeValuesByProductTypeId(Integer productTypeId);
}
