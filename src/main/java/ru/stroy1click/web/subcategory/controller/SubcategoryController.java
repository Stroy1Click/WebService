package ru.stroy1click.web.subcategory.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import ru.stroy1click.web.product.type.dto.ProductTypeDto;
import ru.stroy1click.web.security.SecurityUtils;
import ru.stroy1click.web.subcategory.client.SubcategoryClient;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subcategories")
@RateLimiter(name = "subcategoryLimiter")
public class SubcategoryController {

    private final SubcategoryClient subcategoryClient;

    private final ImageValidatorUtils imageValidator;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public SubcategoryDto get(@PathVariable("id") Integer id){
        return this.subcategoryClient.get(id);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<String> assignImage(@PathVariable("id") Integer id,
                                              @RequestParam("image") MultipartFile image){
        this.imageValidator.validateImage(image);

        this.subcategoryClient.assignImage(id, image, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.subcategory.image.upload",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Integer id,
                                              @RequestParam("link") String link){
        this.subcategoryClient.deleteImage(id, link, SecurityUtils.getJwt());

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.subcategory.image.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @GetMapping("/{id}/product-types")
    public List<ProductTypeDto> getBySubcategory(@PathVariable("id") Integer id){
        return this.subcategoryClient.getProductTypes(id);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid SubcategoryDto subcategoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.subcategoryClient.create(subcategoryDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.subcategory.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid SubcategoryDto subcategoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.subcategoryClient.update(id, subcategoryDto, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.subcategory.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.subcategoryClient.delete(id, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.subcategory.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
