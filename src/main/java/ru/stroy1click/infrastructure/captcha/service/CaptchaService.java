package ru.stroy1click.infrastructure.captcha.service;

public interface CaptchaService {

    boolean validate(String token);
}
