package ru.stroy1click.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.stroy1click.web.order.client.OrderClient;
import ru.stroy1click.web.security.SecurityUtils;
import ru.stroy1click.web.user.client.UserClient;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

    private final UserClient userClient;

    private final OrderClient orderClient;

    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("user", this.userClient.get(SecurityUtils.getUserId()));
        model.addAttribute("orders", this.orderClient.getByUserId(SecurityUtils.getUserId(), SecurityUtils.getJwt()));

        return "user/profile";
    }
}
