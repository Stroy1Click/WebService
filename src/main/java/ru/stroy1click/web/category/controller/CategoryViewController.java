package ru.stroy1click.web.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryViewController {

    @GetMapping
    public String getCategories(){
        return "catalog/category";
    }

    @GetMapping("/{id}/subcategories")
    public String getSubcategoriesOfCategory(@PathVariable("id") Integer id){
        return "catalog/subcategory";
    }
}
