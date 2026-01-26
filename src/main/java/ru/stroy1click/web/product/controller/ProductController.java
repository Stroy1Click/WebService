package ru.stroy1click.web.product.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.common.model.PageResponse;
import ru.stroy1click.web.product.client.ProductClient;
import ru.stroy1click.web.product.dto.ProductDto;
import ru.stroy1click.web.product.dto.ProductImageDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RateLimiter(name = "productLimiter")
public class ProductController {

    private final ProductClient productClient;

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable("id") Integer id){
        return this.productClient.get(id);
    }

    @GetMapping("/{id}/images")
    public List<ProductImageDto> getImages(@PathVariable("id") Integer id){
        return this.productClient.getImages(id);
    }

    @GetMapping
    public PageResponse<ProductDto> getProductsByPagination(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "subcategoryId", required = false) Integer subcategoryId,
            @RequestParam(value = "productTypeId", required = false) Integer productType
    ) {
        return this.productClient.getByPagination(page, size, categoryId, subcategoryId, productType);
    }
}
