package ru.stroy1click.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.stroy1click.domain.notification.client.NotificationClient;
import ru.stroy1click.domain.order.dto.OrderDto;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationClient notificationClient;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrderDto> getNotification() {
        return this.notificationClient.getNewOrders();
    }
}
