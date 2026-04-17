package ru.stroy1click.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    @GetMapping("/{id}")
    public String productPage(@PathVariable("id") Long ignoredId){
        return "catalog/product";
    }

    @GetMapping("/search-results")
    public String searchProductsResult(){
        return "catalog/search-results";
    }
}
