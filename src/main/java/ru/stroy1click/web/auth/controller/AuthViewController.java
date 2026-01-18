package ru.stroy1click.web.auth.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AuthViewController {

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(){
        return "auth/registration";
    }

    @GetMapping("/email/request")
    public String createEmailCode(){
        return "auth/email-request";
    }

    @GetMapping("/email/verify")
    public String verifyEmail(){
        return "auth/email-verify";
    }

    @GetMapping("/forgot-password/request")
    public String createPasswordCode(){
        return "auth/forgot-password-request";
    }

    @GetMapping("/forgot-password/reset")
    public String passwordReset(){
        return "auth/reset-password";
    }

    @GetMapping("/resend-code")
    public String resendEmailCode(@RequestParam(value = "type", required = false) String type){
        return "auth/resend-code";
    }
}
