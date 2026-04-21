package ru.stroy1click.infrastructure.captcha.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stroy1click.infrastructure.captcha.client.YandexCaptchaClient;
import ru.stroy1click.infrastructure.captcha.service.CaptchaService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final YandexCaptchaClient yandexCaptchaClient;

    @Override
    public boolean validate(String token) {
        log.info("validate {}", token);

        return this.yandexCaptchaClient.sendCaptcha(token).isOk();
    }
}
