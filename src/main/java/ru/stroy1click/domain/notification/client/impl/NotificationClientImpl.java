package ru.stroy1click.domain.notification.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.stroy1click.domain.notification.client.NotificationClient;
import ru.stroy1click.domain.order.dto.OrderDto;

@Slf4j
@Service
public class NotificationClientImpl implements NotificationClient {

    private final WebClient webClient;

    public NotificationClientImpl(@Value("${url.notification}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public Flux<OrderDto> getNewOrders() {
        log.error("getNewOrders");

        return this.webClient.get()
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(OrderDto.class);
    }
}
