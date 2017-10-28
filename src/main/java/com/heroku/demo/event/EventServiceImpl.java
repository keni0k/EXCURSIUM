package com.heroku.demo.event;

import java.util.ArrayList;
import java.util.Comparator;
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
    public Event getById(int id) {
        List<Event> list = eventRepository.findAll();
        for (Event aList : list) if (aList.getId() == id) return aList;
        return null;
    }

    @Override
    public Event editEvent(Event event) {
        return eventRepository.saveAndFlush(event);
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> getByFilter(int priceUp, int priceDown, int category, int language, String words) {
        String[] wds = words.split(",");
        List<Event> list = eventRepository.findAll();
        list.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Long.compare(o1.getId(), o2.getId());
            }
        });
        List<Event> copy = new ArrayList<>();
        if (priceUp == -1) priceUp = Integer.MAX_VALUE;
        boolean bool = false;
        if (language == -1) bool = true;
        for (Event aList : list)
            if ((aList.getPrice() >= priceDown) && (aList.getPrice() <= priceUp) &&
                    ((aList.getCategory() == category) || (category == -1)) && ((aList.getRate() == language) || bool)) {
                if (!words.equals("")) {
                    for (String word : wds) {
                        if (aList.getName().toLowerCase().contains(word.toLowerCase()))
                            aList.cnt += 7;
                        if (aList.getDescription().toLowerCase().contains(word.toLowerCase()))
                            aList.cnt += 3;
                    }
                    if (aList.cnt > 0) copy.add(aList);
                } else
                    copy.add(aList);
            }
        if (!words.equals("")) copy.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Integer.compare(o2.cnt, o1.cnt);
            }
        });
        return copy;
    }
}
