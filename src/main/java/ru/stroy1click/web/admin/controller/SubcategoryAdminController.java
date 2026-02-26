package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ImageValidatorUtils;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.security.SecurityUtils;
import ru.stroy1click.web.subcategory.client.SubcategoryClient;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;


@Controller
@RequestMapping("/admin/subcategories")
@RequiredArgsConstructor
public class SubcategoryAdminController {

    private final SubcategoryClient subcategoryClient;

    private final ImageValidatorUtils imageValidatorUtils;

    @GetMapping
    public String subcategoriesPage(Model model){
        model.addAttribute("subcategories", this.subcategoryClient.getAll());
        model.addAttribute("subcategoryDto", new SubcategoryDto());

        return "admin/subcategories";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("subcategoryDto") @Valid SubcategoryDto subcategoryDto,
                         BindingResult bindingResult,
                         @RequestParam("imageFile") MultipartFile image,
                         Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/subcategories";
        }
        this.imageValidatorUtils.validateImage(image);

        try {
            SubcategoryDto createdSubcategory = this.subcategoryClient.create(subcategoryDto, SecurityUtils.getAccessToken());
            this.subcategoryClient.assignImage(createdSubcategory.getId(), image, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/subcategories";
        }

        return "redirect:/admin/subcategories";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("id") Integer id){
        this.subcategoryClient.delete(id, SecurityUtils.getAccessToken());

        return "redirect:/admin/subcategories";
    }
}
