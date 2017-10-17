package com.heroku.demo.event;

import java.util.List;

public interface EventService {
    Event addEvent(Event event);
    void delete(long id);
    List<Event> getByEvent(int guide);
    Event editEvent(Event event);
    List<Event> getAll();
}
