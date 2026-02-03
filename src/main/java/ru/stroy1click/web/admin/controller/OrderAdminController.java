package ru.stroy1click.web.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.order.client.OrderClient;
import ru.stroy1click.web.order.dto.OrderDto;
import ru.stroy1click.web.security.SecurityUtils;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderClient orderClient;

    @GetMapping
    public String ordersPage(Model model){
        model.addAttribute("orders", this.orderClient.getAll(SecurityUtils.getAccessToken()));

        return "admin/orders";
    }

    @GetMapping("/{id}")
    public String orderPage(@PathVariable("id") Long id, Model model){
        model.addAttribute("orderDto", this.orderClient.get(id, SecurityUtils.getAccessToken()));

        return "admin/order";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(
            @PathVariable Long id,
            @ModelAttribute("orderDto") @Valid OrderDto orderDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/order";
        }

        this.orderClient.update(id, orderDto, SecurityUtils.getAccessToken());

        return "redirect:/admin/orders/" + id;
    }
    @GetMapping("/new")
    public String newOrderPage(){
        return "admin/new-orders";
    }
}
