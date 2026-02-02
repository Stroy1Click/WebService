package ru.stroy1click.web.attribute.client;

import ru.stroy1click.web.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.common.dto.PageResponse;
import ru.stroy1click.web.attribute.dto.ProductAttributeValueFilter;

public interface ProductAttributeAssignmentClient extends CrudOperations<ProductAttributeAssignmentDto, Integer> {

    PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(Integer page, Integer size, ProductAttributeValueFilter filter);
}
