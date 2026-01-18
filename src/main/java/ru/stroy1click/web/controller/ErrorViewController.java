package ru.stroy1click.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorViewController {

    @GetMapping("/invalid-session")
    public String invalidSession(){
        return "error/invalid-session";
    }
}
