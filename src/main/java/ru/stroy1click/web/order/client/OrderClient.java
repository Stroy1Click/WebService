package ru.stroy1click.web.order.client;

import ru.stroy1click.web.common.client.CrudOperations;
import ru.stroy1click.web.order.dto.OrderDto;

import java.util.List;

public interface OrderClient {

    OrderDto get(Long id, String jwt);

    List<OrderDto> getAll(String jwt);

    OrderDto create(OrderDto dto, String jwt);

    void update(Long id, OrderDto dto, String jwt);

    void delete(Long id, String jwt);

    List<OrderDto> getByUserId(Long userId, String jwt);
}
