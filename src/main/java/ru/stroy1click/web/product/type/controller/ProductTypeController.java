package ru.stroy1click.web.product.type.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.util.ImageValidatorUtils;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.type.client.ProductTypeClient;
import ru.stroy1click.web.product.type.dto.ProductTypeDto;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-types")
public class ProductTypeController {

    private final ProductTypeClient productTypeClient;

    private final MessageSource messageSource;

    private final ImageValidatorUtils imageValidator;

    @GetMapping("/{id}")
    public ProductTypeDto get(@PathVariable("id") Integer id){
        return this.productTypeClient.get(id);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> assignImage(@PathVariable("id") Integer id,
                                              @RequestParam("image") MultipartFile image){
        this.imageValidator.validateImage(image);

        this.productTypeClient.assignImage(id, image, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type.image.upload",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Integer id,
                                              @RequestParam("link") String link){
        this.productTypeClient.deleteImage(id, link, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type.image.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProductTypeDto productTypeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productTypeClient.create(productTypeDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductTypeDto productTypeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productTypeClient.update(id, productTypeDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productTypeClient.delete(id, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
