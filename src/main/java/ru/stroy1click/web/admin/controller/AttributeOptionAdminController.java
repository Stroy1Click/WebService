package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.attribute.dto.AttributeOptionDto;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.attribute.client.AttributeOptionClient;
import ru.stroy1click.web.security.SecurityUtils;

@Controller
@RequestMapping("/admin/attribute-options")
@RequiredArgsConstructor
public class AttributeOptionAdminController {

    private final AttributeOptionClient attributeOptionClient;

    @GetMapping
    public String productAttributesPage(Model model){
        model.addAttribute("attributeOptionDto", new AttributeOptionDto());
        model.addAttribute("attributeOptions", this.attributeOptionClient.getAll());

        return "admin/attribute-options";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("attributeOptionDto") @Valid AttributeOptionDto attributeOptionDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/attribute-options";
        }

        try {
            this.attributeOptionClient.create(attributeOptionDto, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/attribute-options";
        }

        return "redirect:/admin/attribute-options";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("id") Integer id, Model model){
        try {
            this.attributeOptionClient.delete(id, SecurityUtils.getAccessToken());
        } catch (NotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "admin/attribute-options";
        }
        return "redirect:/admin/attribute-options";
    }
}
