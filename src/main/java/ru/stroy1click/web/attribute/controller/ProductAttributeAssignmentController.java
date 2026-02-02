package ru.stroy1click.web.attribute.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.dto.PageResponse;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.attribute.client.ProductAttributeAssignmentClient;
import ru.stroy1click.web.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.web.attribute.dto.ProductAttributeValueFilter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-attribute-assignments")
@RateLimiter(name = "productAttributeAssignmentLimiter")
public class ProductAttributeAssignmentController {

    private final ProductAttributeAssignmentClient productAttributeAssignmentClient;

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ProductAttributeAssignmentDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.productAttributeAssignmentClient.get(id));
    }

    @PostMapping("/filter")
    public PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                 @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                                                 @RequestBody @Valid ProductAttributeValueFilter filter,
                                                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        return this.productAttributeAssignmentClient.getProductIdsByAttributes(page,size,filter);
    }
}
