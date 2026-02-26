package ru.stroy1click.web.confirmationcode.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stroy1click.common.exception.*;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.confirmationcode.client.ConfirmationCodeClient;
import ru.stroy1click.web.confirmationcode.dto.CodeVerificationRequest;
import ru.stroy1click.web.confirmationcode.dto.CreateConfirmationCodeRequest;
import ru.stroy1click.web.confirmationcode.dto.UpdatePasswordRequest;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/confirmation-codes")
@RequiredArgsConstructor
public class ConfirmationCodeController {

    private final ConfirmationCodeClient confirmationCodeClient;

    private final MessageSource messageSource;

    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid CodeVerificationRequest codeVerificationRequest,
                                               BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.confirmationCodeClient.verifyEmail(codeVerificationRequest);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.confirmation_code.email.confirmed",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest,
                                                 BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.confirmationCodeClient.updatePassword(updatePasswordRequest);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.password.successfully_updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PostMapping("/regeneration")
    public ResponseEntity<String> recreate(@RequestBody @Valid CreateConfirmationCodeRequest codeRequest,
                                           BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.confirmationCodeClient.recreate(codeRequest);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.confirmation_code.sent",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateConfirmationCodeRequest codeRequest,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.confirmationCodeClient.create(codeRequest);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.confirmation_code.sent",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
