package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.attribute.client.ProductAttributeAssignmentClient;
import ru.stroy1click.web.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.web.security.SecurityUtils;

@Controller
@RequestMapping("/admin/product-attribute-assignments")
@RequiredArgsConstructor
public class ProductAttributeAssignmentAdminController {

    private final ProductAttributeAssignmentClient productAttributeAssignmentClient;

    @GetMapping
    public String productAttributesPage(Model model){
        model.addAttribute("productAttributeAssignmentDto", new ProductAttributeAssignmentDto());
        model.addAttribute("productAttributeAssignments", this.productAttributeAssignmentClient.getAll());

        return "admin/product-attribute-assignments";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("productAttributeAssignmentDto") @Valid ProductAttributeAssignmentDto attributeDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/product-attribute-assignments";
        }

        try {
            this.productAttributeAssignmentClient.create(attributeDto, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/product-attribute-assignments";
        }

        return "redirect:/admin/product-attribute-assignments";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("id") Integer id){
        this.productAttributeAssignmentClient.delete(id, SecurityUtils.getAccessToken());

        return "redirect:/admin/product-types";
    }
}
