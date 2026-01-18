package ru.stroy1click.web.category.client;

import ru.stroy1click.web.category.dto.CategoryDto;
import ru.stroy1click.web.common.client.ResourceClient;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.util.List;

public interface CategoryClient extends ResourceClient<CategoryDto, Integer> {

    List<CategoryDto> getAll();

    List<SubcategoryDto> getSubcategories(Integer id);
}
