package ru.stroy1click.web.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.auth.client.AuthClient;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.user.dto.UserDto;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthClient authClient;

    private final MessageSource messageSource;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(
                ValidationErrorUtils.collectErrorsToString(bindingResult.getFieldErrors())
        );

        userDto.setEmailConfirmed(false);
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
