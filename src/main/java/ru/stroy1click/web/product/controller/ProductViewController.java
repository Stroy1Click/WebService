package ru.stroy1click.web.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") Long id){
        return "catalog/product-info";
    }
}
