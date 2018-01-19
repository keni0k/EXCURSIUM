package com.heroku.demo.order;

import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.photo.PhotoServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private EventServiceImpl eventService;
    private PhotoServiceImpl photoService;

    @Override
    public Buy addOrder(Buy order) {
        return orderRepository.saveAndFlush(order);
    }

    @Override
    public void delete(long id) {
        orderRepository.delete(id);
    }

    public OrderServiceImpl(OrderRepository orderRepository, EventServiceImpl eventService) {
        this.orderRepository = orderRepository;
        this.eventService = eventService;
        this.photoService = eventService.getPhotoService();
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
            if (order.getTouristId() == touristId) {
                copy.add(order);
            }
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

    @Override
    public Buy getById(long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public Boolean findByOrder(long personId, long orderId) {
        return getById(orderId).getTouristId()==personId;
    }

    @Override
    public boolean findByReview(long orderId, long reviewId) {
        return getById(orderId).getReviewId()==-1||getById(orderId).getReviewId()==reviewId;
    }
}
