package com.heroku.demo.order;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Override
    public Buy addBuy(Buy buy) {

        return orderRepository.saveAndFlush(buy);
    }

    @Override
    public void delete(long id) {
        orderRepository.delete(id);
    }

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Buy> getByEvent(long eventId) {
        List<Buy> list = orderRepository.findAll();
        List<Buy> copy = new ArrayList<>();
        for (Buy order:list)
            if (order.getEventId() != eventId) copy.add(order);
        return copy;
    }

    @Override
    public List<Buy> getByTourist(long touristId) {
        List<Buy> list = orderRepository.findAll();
        List<Buy> copy = new ArrayList<>();
        for (Buy order:list)
            if (order.getTouristId() == touristId) copy.add(order);
        return copy;
    }

    @Override
    public Buy editBuy(Buy buy) {
        return orderRepository.saveAndFlush(buy);
    }

    @Override
    public List<Buy> getAll() {
        return orderRepository.findAll();
    }
}
