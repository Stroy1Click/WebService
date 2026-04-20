package ru.stroy1click.web.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.domain.auth.client.AuthClient;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.domain.common.util.ValidationErrorUtils;
import ru.stroy1click.domain.user.dto.UserDto;
import ru.stroy1click.infrastructure.captcha.service.CaptchaService;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final AuthClient authClient;

    private final MessageSource messageSource;

    private final CaptchaService captchaService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid UserDto userDto,
                                               BindingResult bindingResult,
                                               @RequestParam("smart-token") String token) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(
                ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors())
        );

        if(!this.captchaService.validate(token)) {
            throw new ValidationException(
                    this.messageSource.getMessage(
                            "validation.captcha.invalid",
                            null,
                            Locale.getDefault()
                    )
            );
        }

        userDto.setIsEmailConfirmed(false);
        this.authClient.registration(userDto);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.auth.registration",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
