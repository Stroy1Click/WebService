package ru.stroy1click.web.attribute.client;

import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.attribute.dto.AttributeOptionDto;

import java.util.List;

public interface AttributeOptionClient
        extends CrudOperations<AttributeOptionDto, Integer> {

    List<AttributeOptionDto> getProductTypeAttributeValuesByProductTypeId(Integer productTypeId);
}
