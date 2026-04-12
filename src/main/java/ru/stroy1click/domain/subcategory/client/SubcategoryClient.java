package ru.stroy1click.domain.subcategory.client;

import ru.stroy1click.domain.common.client.ResourceClient;
import ru.stroy1click.domain.product.type.dto.ProductTypeDto;
import ru.stroy1click.domain.subcategory.dto.SubcategoryDto;

import java.util.List;

public interface SubcategoryClient extends ResourceClient<SubcategoryDto, Integer> {

    List<ProductTypeDto> getProductTypes(Integer id);
}
