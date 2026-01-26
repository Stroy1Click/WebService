package ru.stroy1click.web.category.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.web.category.client.CategoryClient;
import ru.stroy1click.web.category.dto.CategoryDto;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@RateLimiter(name = "categoryLimiter")
public class CategoryController {

    private final CategoryClient categoryClient;

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable("id") Integer id){
        return this.categoryClient.get(id);
    }

    @GetMapping
    public List<CategoryDto> getCategories(){
        return this.categoryClient.getAll();
    }

    @GetMapping("/{id}/subcategories")
    public List<SubcategoryDto> getSubcategories(@PathVariable("id") Integer id){
        return this.categoryClient.getSubcategories(id);
    }
}
