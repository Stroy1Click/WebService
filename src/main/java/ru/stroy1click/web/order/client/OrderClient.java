package ru.stroy1click.web.order.client;

import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.order.dto.OrderDto;

import java.util.List;

public interface OrderClient extends CrudOperations<OrderDto, Long> {

    List<OrderDto> getByUserId(Long userId, String jwt);
}
