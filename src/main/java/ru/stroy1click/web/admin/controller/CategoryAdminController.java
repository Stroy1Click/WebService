package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.web.category.client.CategoryClient;
import ru.stroy1click.web.category.dto.CategoryDto;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ImageValidatorUtils;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.security.SecurityUtils;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryClient categoryClient;

    private final ImageValidatorUtils imageValidatorUtils;

    @GetMapping
    public String categoriesPage(Model model){
        model.addAttribute("categoryDto", new CategoryDto());
        model.addAttribute("categories", this.categoryClient.getAll());

        return "admin/categories";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("categoryDto") @Valid CategoryDto categoryDto,
                                 BindingResult bindingResult,
                                 @RequestParam("imageFile") MultipartFile image,
                                 Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/categories";
        }
        this.imageValidatorUtils.validateImage(image);

        try {
            CategoryDto createdCategory = this.categoryClient.create(categoryDto, SecurityUtils.getAccessToken());
            this.categoryClient.assignImage(createdCategory.getId(), image, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/categories";
        }

        return "redirect:/admin/categories";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("id") Integer id){
        this.categoryClient.delete(id, SecurityUtils.getAccessToken());

        return "redirect:/admin/categories";
    }
}
