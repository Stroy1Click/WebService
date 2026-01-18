package ru.stroy1click.web.product.type.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product-types")
public class ProductTypeViewController {

    @GetMapping("/{id}/products")
    public String getProducts(@PathVariable("id") Integer id){
        return "catalog/product";
    }
}
