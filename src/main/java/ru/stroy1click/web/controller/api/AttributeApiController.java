package ru.stroy1click.web.controller.api;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.domain.attribute.client.AttributeClient;
import ru.stroy1click.domain.attribute.dto.AttributeDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attributes")
@RateLimiter(name = "attributeLimiter")
public class AttributeApiController {

    private final AttributeClient attributeClient;

    @GetMapping("/{id}")
    public ResponseEntity<AttributeDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.attributeClient.get(id));
    }
}
