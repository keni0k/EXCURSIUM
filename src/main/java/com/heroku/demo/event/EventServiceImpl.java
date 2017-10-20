package com.heroku.demo.event;

import com.heroku.demo.message.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements EventService {

    public EventRepository getMessageRepository() {
        return eventRepository;
    }

    private EventRepository eventRepository;

    @Override
    public Event addEvent(Event message) {

        return eventRepository.saveAndFlush(message);
    }

    @Override
    public void delete(long id) {
        eventRepository.delete(id);
    }

    public EventServiceImpl(EventRepository reviewRepository) {
        this.eventRepository = reviewRepository;
    }

    @Override
    public List<Event> getById(int id) {
        List<Event> list = eventRepository.findAll();
        List<Event> copy = new ArrayList<>();
        copy.addAll(list);
        for (int i = 0; i<copy.size(); i++)
            if (copy.get(i).getId()!=id) list.remove(i);
        return list;
    }

    @Override
    public Event editEvent(Event event)
    {
        return eventRepository.saveAndFlush(event);
    }

    @Override
    public List<Event> getAll()
    {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getByFilter(int priceUp, int priceDown, int category, int language) {
        List<Event> list = eventRepository.findAll();
        List<Event> copy = new ArrayList<>();
        if (priceUp == -1) priceUp = Integer.MAX_VALUE;
        for (Event aList : list)
            if ((aList.getPrice() >= priceDown) && (aList.getPrice() <= priceUp) && ((aList.getCategory() == category)||(category==-1)) && (aList.getRate()==language))
                copy.add(aList);
        return copy;
    }
}
