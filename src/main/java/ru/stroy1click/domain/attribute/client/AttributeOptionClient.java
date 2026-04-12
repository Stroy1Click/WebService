package ru.stroy1click.domain.attribute.client;

import ru.stroy1click.domain.common.client.CrudOperations;
import ru.stroy1click.domain.attribute.dto.AttributeOptionDto;

import java.util.List;

public interface AttributeOptionClient
        extends CrudOperations<AttributeOptionDto, Integer> {

    List<AttributeOptionDto> getProductTypeAttributeValuesByProductTypeId(Integer productTypeId);
}
