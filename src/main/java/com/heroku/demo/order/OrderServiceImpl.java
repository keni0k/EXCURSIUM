package com.heroku.demo.order;

import com.heroku.demo.event.Event;
import com.heroku.demo.event.EventRepository;
import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private EventServiceImpl eventService;
    private PhotoServiceImpl photoService;

    @Override
    public Buy addBuy(Buy buy) {

        return orderRepository.saveAndFlush(buy);
    }

    @Override
    public void delete(long id) {
        orderRepository.delete(id);
    }

    public OrderServiceImpl(OrderRepository orderRepository, EventRepository eventRepository,
                            PhotoRepository photoRepository) {
        this.orderRepository = orderRepository;
        photoService = new PhotoServiceImpl(photoRepository);
        eventService = new EventServiceImpl(eventRepository, photoService);
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
                Event e = eventService.getById(order.getEventId());
                order.setName(e.getName());
                order.setImageUrl(photoService.getByEventId(e.getId()).getData());
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
}
