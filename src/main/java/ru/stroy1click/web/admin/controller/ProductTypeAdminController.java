package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.web.common.exception.AlreadyExistsException;
import ru.stroy1click.web.common.exception.NotFoundException;
import ru.stroy1click.web.common.util.ImageValidatorUtils;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.type.client.ProductTypeClient;
import ru.stroy1click.web.product.type.dto.ProductTypeDto;
import ru.stroy1click.web.security.SecurityUtils;


@Controller
@RequestMapping("/admin/product-types")
@RequiredArgsConstructor
public class ProductTypeAdminController {

    private final ProductTypeClient productTypeClient;

    private final ImageValidatorUtils imageValidatorUtils;

    @GetMapping
    public String productTypesPage(Model model){
        model.addAttribute("productTypes", this.productTypeClient.getAll());
        model.addAttribute("productTypeDto", new ProductTypeDto());

        return "admin/product-types";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("productTypeDto") @Valid ProductTypeDto productTypeDto,
                         BindingResult bindingResult,
                         @RequestParam("imageFile") MultipartFile image,
                         Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/product-types";
        }
        this.imageValidatorUtils.validateImage(image);

        try {
            ProductTypeDto createdProductType = this.productTypeClient.create(productTypeDto, SecurityUtils.getAccessToken());
            this.productTypeClient.assignImage(createdProductType.getId(), image, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/product-types";
        }

        return "redirect:/admin/product-types";
    }
}