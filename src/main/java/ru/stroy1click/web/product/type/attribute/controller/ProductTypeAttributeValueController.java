package ru.stroy1click.web.product.type.attribute.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.product.type.attribute.client.ProductTypeAttributeValueClient;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.type.attribute.dto.ProductTypeAttributeValueDto;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-type-attribute-values")
@RateLimiter(name = "productTypeAttributeLimiter")
public class ProductTypeAttributeValueController {

    private final ProductTypeAttributeValueClient productTypeAttributeValueClient;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeAttributeValueDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.productTypeAttributeValueClient.get(id));
    }

    @GetMapping
    public List<ProductTypeAttributeValueDto> getProductTypeAttributeValuesByProductTypeId(@RequestParam("productTypeId") Integer productTypeId){
        return this.productTypeAttributeValueClient.getProductTypeAttributeValuesByProductTypeId(productTypeId);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProductTypeAttributeValueDto productTypeAttributeValueDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productTypeAttributeValueClient.create(productTypeAttributeValueDto, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type_attribute_value.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductTypeAttributeValueDto productTypeAttributeValueDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productTypeAttributeValueClient.update(id, productTypeAttributeValueDto, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type_attribute_value.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productTypeAttributeValueClient.delete(id, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type_attribute_value.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
