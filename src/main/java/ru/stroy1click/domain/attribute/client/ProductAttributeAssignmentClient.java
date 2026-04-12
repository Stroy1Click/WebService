package ru.stroy1click.domain.attribute.client;

import ru.stroy1click.domain.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.domain.common.client.CrudOperations;
import ru.stroy1click.domain.common.dto.PageResponse;
import ru.stroy1click.domain.attribute.dto.ProductAttributeValueFilter;

public interface ProductAttributeAssignmentClient extends CrudOperations<ProductAttributeAssignmentDto, Integer> {

    PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(Integer page, Integer size, ProductAttributeValueFilter filter);
}
