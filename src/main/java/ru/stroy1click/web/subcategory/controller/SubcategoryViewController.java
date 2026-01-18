package ru.stroy1click.web.subcategory.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subcategories")
public class SubcategoryViewController {

    @GetMapping("/{id}/product-types")
    public String getProductTypes(@PathVariable("id") Integer id){
        return "catalog/product-type";
    }
}
