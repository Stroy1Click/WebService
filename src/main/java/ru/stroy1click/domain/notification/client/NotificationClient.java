package ru.stroy1click.domain.notification.client;


import reactor.core.publisher.Flux;
import ru.stroy1click.domain.order.dto.OrderDto;

public interface NotificationClient {

   Flux<OrderDto> getNewOrders();
}
