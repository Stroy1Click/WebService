package ru.stroy1click.web.product.type.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.web.product.type.client.ProductTypeClient;
import ru.stroy1click.web.product.type.dto.ProductTypeDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-types")
public class ProductTypeController {

    private final ProductTypeClient productTypeClient;

    @GetMapping("/{id}")
    public ProductTypeDto get(@PathVariable("id") Integer id){
        return this.productTypeClient.get(id);
    }
}
