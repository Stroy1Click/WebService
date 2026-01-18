package ru.stroy1click.web.product.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.model.PageResponse;
import ru.stroy1click.web.common.util.ImageValidatorUtils;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.product.client.ProductClient;
import ru.stroy1click.web.product.dto.ProductDto;
import ru.stroy1click.web.product.dto.ProductImageDto;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RateLimiter(name = "productLimiter")
public class ProductController {

    private final ProductClient productClient;

    private final MessageSource messageSource;

    private final ImageValidatorUtils imageValidator;

    @GetMapping("/{id}")
    public ProductDto get(@PathVariable("id") Integer id){
        return this.productClient.get(id);
    }

    @GetMapping("/{id}/images")
    public List<ProductImageDto> getImages(@PathVariable("id") Integer id){
        return this.productClient.getImages(id);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<String> assignImages(@PathVariable("id") Integer id,
                                               @RequestParam("images") List<MultipartFile> images){
        this.imageValidator.validateImages(images);

        this.productClient.assignImages(id, images, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product.images.upload",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}/images")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Integer id,
                                              @RequestParam("link") String link){
        this.productClient.deleteImage(id,link, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product.image.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @GetMapping
    public PageResponse<ProductDto> getProductsByPagination(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "subcategoryId", required = false) Integer subcategoryId,
            @RequestParam(value = "productTypeId", required = false) Integer productType
    ) {
        return this.productClient.getByPagination(page, size, categoryId, subcategoryId, productType);
    }


    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productClient.create(productDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductDto productDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));


        this.productClient.update(id, productDto, "");

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productClient.delete(id, "");
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
