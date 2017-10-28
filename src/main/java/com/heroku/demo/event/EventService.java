package com.heroku.demo.event;

import java.util.List;

public interface EventService {
    Event addEvent(Event event);

    void delete(long id);

    Event getById(long id);

    Event editEvent(Event event);

    List<Event> getAll();

    List<Event> getByFilter(int priceUp, int priceDown, int category, int language, String words);
}
