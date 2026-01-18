package ru.stroy1click.web.attribute.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.attribute.client.AttributeClient;
import ru.stroy1click.web.attribute.dto.AttributeDto;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attributes")
@RateLimiter(name = "attributeLimiter")
public class AttributeController {

    private final AttributeClient attributeClient;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.attributeClient.get(id));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid AttributeDto attributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.attributeClient.create(attributeDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.attribute.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid AttributeDto attributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.attributeClient.update(id, attributeDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.attribute.update",
                        null,
                        Locale.getDefault()
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.attributeClient.delete(id, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.attribute.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
