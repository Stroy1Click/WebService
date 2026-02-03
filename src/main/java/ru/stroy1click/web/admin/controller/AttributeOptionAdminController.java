package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.stroy1click.web.attribute.dto.AttributeOptionDto;
import ru.stroy1click.web.common.exception.AlreadyExistsException;
import ru.stroy1click.web.common.exception.NotFoundException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.attribute.client.AttributeOption;
import ru.stroy1click.web.security.SecurityUtils;

@Controller
@RequestMapping("/admin/attribute-options")
@RequiredArgsConstructor
public class AttributeOptionAdminController {

    private final AttributeOption attributeOption;

    @GetMapping
    public String productAttributesPage(Model model){
        model.addAttribute("attributeOptionDto", new AttributeOptionDto());
        model.addAttribute("attributeOptions", this.attributeOption.getAll());

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
            this.attributeOption.create(attributeOptionDto, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/attribute-options";
        }

        return "redirect:/admin/attribute-options";
    }
}
