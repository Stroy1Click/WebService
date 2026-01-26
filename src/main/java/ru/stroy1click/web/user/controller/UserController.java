package ru.stroy1click.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserClient userClient;


    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id){
        return this.userClient.get(id);
    }

    @GetMapping("/email")
    public UserDto getByEmail(@RequestParam("email") String email){
        return this.userClient.getByEmail(email);
    }
}
