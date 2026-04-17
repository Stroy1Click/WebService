package ru.stroy1click.web.controller.view;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subcategories")
public class SubcategoryViewController {

    @GetMapping("/{id}/product-types")
    public String productTypesPage(@PathVariable("id") Integer ignoredId){
        return "catalog/product-types";
    }
}
