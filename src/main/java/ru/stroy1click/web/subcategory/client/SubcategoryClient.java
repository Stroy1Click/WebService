package ru.stroy1click.web.subcategory.client;

import ru.stroy1click.web.common.client.ResourceClient;
import ru.stroy1click.web.product.type.dto.ProductTypeDto;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.util.List;

public interface SubcategoryClient extends ResourceClient<SubcategoryDto, Integer> {

    List<ProductTypeDto> getProductTypes(Integer id);
}
