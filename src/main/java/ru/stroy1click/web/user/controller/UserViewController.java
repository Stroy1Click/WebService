package ru.stroy1click.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.order.client.OrderClient;
import ru.stroy1click.web.security.SecurityUtils;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

    private final UserClient userClient;

    private final OrderClient orderClient;

    @GetMapping("/profile")
    public String profilePage(Model model){
        model.addAttribute("user", this.userClient.get(SecurityUtils.getUserId(), SecurityUtils.getAccessToken()));
        model.addAttribute("orders", this.orderClient.getByUserId(SecurityUtils.getUserId(), SecurityUtils.getAccessToken()));

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
