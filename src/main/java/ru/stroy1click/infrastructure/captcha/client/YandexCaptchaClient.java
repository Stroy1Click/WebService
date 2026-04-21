package ru.stroy1click.infrastructure.captcha.client;


import ru.stroy1click.infrastructure.captcha.dto.SmartCaptchaResponse;

public interface YandexCaptchaClient {

    SmartCaptchaResponse sendCaptcha(String token);
}
