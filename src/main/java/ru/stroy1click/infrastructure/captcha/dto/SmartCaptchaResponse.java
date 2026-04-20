package ru.stroy1click.infrastructure.captcha.dto;

public record SmartCaptchaResponse(
        String status
) {

    public boolean isOk() {
        return status.equals("ok");
    }
}
