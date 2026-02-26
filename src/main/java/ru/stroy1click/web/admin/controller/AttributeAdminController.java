package ru.stroy1click.web.admin.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.attribute.client.AttributeClient;
import ru.stroy1click.web.attribute.dto.AttributeDto;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.security.SecurityUtils;

@Controller
@RequestMapping("/admin/attributes")
@RequiredArgsConstructor
public class AttributeAdminController {

    private final AttributeClient attributeClient;

    @GetMapping
    public String attributesPage(Model model){
        model.addAttribute("attributeDto", new AttributeDto());
        model.addAttribute("attributes", this.attributeClient.getAll());

        return "admin/attributes";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("attributeDto") @Valid AttributeDto attributeDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("error", ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors()));
            return "admin/attributes";
        }

        try {
            this.attributeClient.create(attributeDto, SecurityUtils.getAccessToken());
        } catch (NotFoundException | AlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "admin/attributes";
        }

        return "redirect:/admin/attributes";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("id") Integer id, Model model){
        try {
            this.attributeClient.delete(id, SecurityUtils.getAccessToken());
        } catch (NotFoundException e){
            model.addAttribute("notFoundError", e.getMessage());
            return "admin/attributes";
        }

        return "redirect:/admin/attributes";
    }
}
