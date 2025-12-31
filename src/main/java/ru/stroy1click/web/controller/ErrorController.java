package ru.stroy1click.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/invalid-session")
    public String invalidSession(){
        return "error/invalid-session";
    }

    @GetMapping("/not-found")
    public String notFound(){
        return "error/404";
    }

    @GetMapping("/internal-exception")
    public String internalException(){
        return "error/500";
    }
}
