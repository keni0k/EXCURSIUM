package com.heroku.demo.order;

import java.util.List;

public interface OrderService {
    Buy addOrder(Buy buy);

    void delete(long id);

    List<Buy> getByEvent(long eventId);

    List<Buy> getByTourist(long touristId);

    Buy editBuy(Buy buy);

    List<Buy> getAll();

    Buy getById(long id);

    Boolean findByOrder(long personId, long orderId);
}
