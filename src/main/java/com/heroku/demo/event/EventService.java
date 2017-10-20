package com.heroku.demo.event;

import java.util.List;

public interface EventService {
    Event addEvent(Event event);
    void delete(long id);
    List<Event> getById(int guide);
    Event editEvent(Event event);
    List<Event> getAll();
    List<Event> getByFilter(int priceUp, int priceDown, int category, int language);
}
