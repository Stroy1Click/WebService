package ru.stroy1click.web.category.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.web.category.client.CategoryClient;
import ru.stroy1click.web.category.dto.CategoryDto;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.util.ImageValidatorUtils;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.subcategory.dto.SubcategoryDto;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@RateLimiter(name = "categoryLimiter")
public class CategoryController {

    private final CategoryClient categoryClient;

    private final ImageValidatorUtils imageValidator;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable("id") Integer id){
        return this.categoryClient.get(id);
    }

    @GetMapping
    public List<CategoryDto> getCategories(){
        return this.categoryClient.getAll();
    }

    @GetMapping("/{id}/subcategories")
    public List<SubcategoryDto> getSubcategories(@PathVariable("id") Integer id){
        return this.categoryClient.getSubcategories(id);
    }


    @PostMapping("/{id}/image")
    public ResponseEntity<String> assignImage(@PathVariable("id") Integer id, @RequestParam("image")
    MultipartFile image){
        this.imageValidator.validateImage(image);

        this.categoryClient.assignImage(id, image, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.category.image.upload",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Integer id,
                                              @RequestParam("link") String link){
        this.categoryClient.deleteImage(id, link, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.category.image.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CategoryDto categoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.categoryClient.create(categoryDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.category.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid CategoryDto categoryDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.categoryClient.update(id, categoryDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.category.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.categoryClient.delete(id, "");

        return ResponseEntity.status(HttpStatus.OK).body(
                this.messageSource.getMessage(
                        "info.category.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
