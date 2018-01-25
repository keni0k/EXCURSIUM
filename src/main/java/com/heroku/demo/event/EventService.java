package com.heroku.demo.event;

import java.util.List;

public interface EventService {
    Event addEvent(Event event);

    void delete(long id);

    Event getById(long id);

    Event editEvent(Event event);

    List<Event> getAll();

    List<Event> getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, Integer country, Integer city, String words, boolean isAll);

    List<Event> getByGuideId(long guideId);

    void setRate(int rate, long eventId);
}
