package ru.stroy1click.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.document.client.DocumentClient;
import ru.stroy1click.web.document.dto.DocumentDto;
import ru.stroy1click.web.order.client.OrderClient;
import ru.stroy1click.web.order.dto.OrderDto;
import ru.stroy1click.web.security.SecurityUtils;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

    private final UserClient userClient;

    private final OrderClient orderClient;

    private final DocumentClient documentClient;

    @GetMapping("/profile")
    public String profilePage(Model model){
        List<OrderDto> orders = this.orderClient.getByUserId(SecurityUtils.getUserId(), SecurityUtils.getAccessToken());
        List<DocumentDto> documents = this.documentClient.getAllByUserId(SecurityUtils.getUserId(), SecurityUtils.getAccessToken());
        model.addAttribute("user",  this.userClient.get(SecurityUtils.getUserId(), SecurityUtils.getAccessToken()));

        if(!orders.isEmpty()){
            model.addAttribute("orders", orders);
        } else {
            model.addAttribute("noOrders", orders);
        }

        if(!documents.isEmpty()){
            model.addAttribute("documents", documents);
        } else {
            model.addAttribute("noDocuments", documents);
        }

        return "user/profile";
    }

    @GetMapping("/update")
    public String updatePage(Model model){
        model.addAttribute("user", this.userClient.get(SecurityUtils.getUserId(), SecurityUtils.getAccessToken()));

        return "/user/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") UserDto userDto){
        this.userClient.update(userDto.getId(), userDto, SecurityUtils.getAccessToken());

        return "redirect:/user/profile";
    }
}
