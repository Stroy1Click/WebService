package ru.stroy1click.web.product.attribute.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.model.PageResponse;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.attribute.client.ProductAttributeClient;
import ru.stroy1click.web.product.attribute.dto.ProductAttributeDto;
import ru.stroy1click.web.product.attribute.model.ProductAttributeValueFilter;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-attributes")
@RateLimiter(name = "productAttributeValueLimiter")
public class ProductAttributeController {

    private final ProductAttributeClient productAttributeClient;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.productAttributeClient.get(id));
    }

    @PostMapping("/filter")
    public PageResponse<ProductAttributeDto> getProductIdsByAttributes(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                                       @RequestBody @Valid ProductAttributeValueFilter filter,
                                                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        return this.productAttributeClient.getProductIdsByAttributes(page,size,filter);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProductAttributeDto productAttributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productAttributeClient.create(productAttributeDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductAttributeDto productAttributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productAttributeClient.update(id, productAttributeDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productAttributeClient.delete(id, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
