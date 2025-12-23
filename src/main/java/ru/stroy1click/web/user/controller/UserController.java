package ru.stroy1click.web.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.user.client.UserClient;
import ru.stroy1click.web.user.dto.UserDto;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserClient userClient;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id){
        return this.userClient.get(id);
    }

    @GetMapping("/email")
    public UserDto getByEmail(@RequestParam("email") String email){
        return this.userClient.getByEmail(email);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.userClient.update(id, userDto, "jwt");
        return ResponseEntity.status(HttpStatus.OK).body(
                this.messageSource.getMessage(
                        "info.user.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        this.userClient.delete(id, "jwt");
        return ResponseEntity.status(HttpStatus.OK).body(
                this.messageSource.getMessage(
                        "info.user.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
