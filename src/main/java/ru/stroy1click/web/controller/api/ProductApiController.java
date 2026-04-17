package ru.stroy1click.web.controller.api;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.domain.common.dto.PageResponse;
import ru.stroy1click.domain.product.client.ProductClient;
import ru.stroy1click.domain.product.dto.ProductDto;
import ru.stroy1click.domain.product.dto.ProductImageDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RateLimiter(name = "productLimiter")
public class ProductApiController {

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
