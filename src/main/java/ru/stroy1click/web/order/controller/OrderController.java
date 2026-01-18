package ru.stroy1click.web.order.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.web.common.exception.ValidationException;
import ru.stroy1click.web.common.util.ValidationErrorUtils;
import ru.stroy1click.web.order.client.OrderClient;
import ru.stroy1click.web.order.dto.OrderDto;
import ru.stroy1click.web.security.SecurityUtils;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@RateLimiter(name = "orderLimiter")
public class OrderController {

    private final MessageSource messageSource;

    private final OrderClient orderClient;


    @GetMapping("/{id}")
    public OrderDto get(@PathVariable("id") Long id){
        return this.orderClient.get(id);
    }

    @GetMapping("/user")
    public List<OrderDto> getByUserId(@RequestParam("userId") Long userId){
        return this.orderClient.getByUserId(userId, SecurityUtils.getJwt());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid OrderDto orderDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        orderDto.setUserId(SecurityUtils.getUserId());

        this.orderClient.create(orderDto, SecurityUtils.getJwt());

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.order.create",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id,
                                         @RequestBody @Valid OrderDto orderDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.orderClient.update(id, orderDto, SecurityUtils.getJwt());

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.order.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        this.orderClient.delete(id, SecurityUtils.getJwt());

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.order.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }

}