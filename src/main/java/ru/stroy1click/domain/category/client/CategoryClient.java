package ru.stroy1click.domain.category.client;

import ru.stroy1click.domain.category.dto.CategoryDto;
import ru.stroy1click.domain.common.client.ResourceClient;
import ru.stroy1click.domain.subcategory.dto.SubcategoryDto;

import java.util.List;

public interface CategoryClient extends ResourceClient<CategoryDto, Integer> {

    List<SubcategoryDto> getSubcategories(Integer id);
}
