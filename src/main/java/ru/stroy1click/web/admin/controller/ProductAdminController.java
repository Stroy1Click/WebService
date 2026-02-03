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
import ru.stroy1click.web.product.client.ProductClient;
import ru.stroy1click.web.product.dto.ProductDto;
import ru.stroy1click.web.security.SecurityUtils;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductClient productClient;

    private final ImageValidatorUtils imageValidatorUtils;

    @GetMapping
    public String productsPage(Model model){
        model.addAttribute("productDto", new ProductDto());

        return "admin/products";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("productDto") @Valid ProductDto productDto,
                         BindingResult bindingResult,
                         @RequestParam("imageFiles") List<MultipartFile> images,
                         Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/products";
        }
        this.imageValidatorUtils.validateImages(images);

        try {
            ProductDto createdProduct = this.productClient.create(productDto, SecurityUtils.getAccessToken());
            this.productClient.assignImages(createdProduct.getId(), images, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/products";
        }

        return "redirect:/admin/products";
    }
}
