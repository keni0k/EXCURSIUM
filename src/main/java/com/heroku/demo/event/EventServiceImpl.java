package com.heroku.demo.event;

import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoServiceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventServiceImpl implements EventService {

    public EventRepository getMessageRepository() {
        return eventRepository;
    }

    private EventRepository eventRepository;
    private PersonServiceImpl personService;
    private PhotoServiceImpl photoService;

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(long id) {
        eventRepository.delete(id);
    }

    public EventServiceImpl(EventRepository reviewRepository, PersonServiceImpl personService, PhotoServiceImpl photoService) {
        this.personService = personService;
        this.eventRepository = reviewRepository;
        this.photoService = photoService;
    }

    @Override
    public Event getById(long id) {
        Event e = eventRepository.findOne(id);
        Person p = personService.getById(e.getGuideId());
        if (p!=null) {
            e.photoOfGuide = p.getImageUrl();
            e.fullNameOfGuide = p.getFirstName() + " " + p.getLastName();
        }
        Photo img = photoService.getByEventId(id);
        if (img!=null)
            e.pathToPhoto="https://excursium.blob.core.windows.net/img/"+img.getData();
        return e;
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
    public List<Event> getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, String words, boolean isSort) {

        if (words==null) words = "";
        if (priceUp==null) priceDown = -1;
        if (priceUp==null) priceUp = -1;
        if (category==null) category = -1;
        if (language==null) language = -1;

        String[] wds = words.split(",");
        List<Event> list = eventRepository.findAll();

        if (words.equals("") && isSort)
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
                    ((aList.getCategory() == category) || (category == -1)) && ((aList.getLanguage() == language) || bool) && aList.getType()==0) {

                Person p = personService.getById(aList.getGuideId());
                if (p!=null) {
                    aList.fullNameOfGuide = p.getFirstName() + " " + p.getLastName();
                    aList.photoOfGuide = p.getImageUrl();
                }

                Photo img = photoService.getByEventId(aList.getId());
                if (img!=null)
                    aList.pathToPhoto="https://excursium.blob.core.windows.net/img/"+img.getData();

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

    public List<Event> getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, String words, Integer sortBy) {

        List<Event> events = getByFilter(priceUp, priceDown, category, language, words, false);
        if (sortBy==null) return events;
        switch (sortBy){
            case 1:break;
            case 2:break;
        }
        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                switch (sortBy) {
                    case 0: return Long.compare(o1.getId(), o1.getId());
                    case 1: return Long.compare(o1.getId(), o1.getId());
                    case 2: return Long.compare(o1.getId(), o1.getId());
                    case 3: return Long.compare(o1.getId(), o1.getId());
                    case 4: return Long.compare(o1.getId(), o1.getId());
                }
                return Long.compare(o1.getId(), o2.getId());
            }
        });
        return events;
    }
}
