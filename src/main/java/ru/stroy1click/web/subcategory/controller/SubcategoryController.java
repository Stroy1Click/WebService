package ru.stroy1click.web.subcategory.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.web.product.type.dto.ProductTypeDto;
import ru.stroy1click.web.subcategory.client.SubcategoryClient;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subcategories")
@RateLimiter(name = "subcategoryLimiter")
public class SubcategoryController {

    private final SubcategoryClient subcategoryClient;


    @GetMapping("/{id}")
    public SubcategoryDto get(@PathVariable("id") Integer id){
        return this.subcategoryClient.get(id);
    }

    @GetMapping("/{id}/product-types")
    public List<ProductTypeDto> getBySubcategory(@PathVariable("id") Integer id){
        return this.subcategoryClient.getProductTypes(id);
    }
}
