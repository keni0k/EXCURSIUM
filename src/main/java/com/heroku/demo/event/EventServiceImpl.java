package com.heroku.demo.event;

import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.utils.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private PhotoServiceImpl photoService;
    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    public PhotoServiceImpl getPhotoService(){
        return photoService;
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(long id) {
        eventRepository.delete(id);
    }

    public EventServiceImpl(EventRepository eventRepository, PhotoServiceImpl photoService) {
        this.eventRepository = eventRepository;
        this.photoService = photoService;
    }

    @Override
    public Event getById(long id) {
        Event e = eventRepository.findOne(id);
        Photo img = photoService.getByEventId(id);
        if (img!=null)
            e.pathToPhoto=img.getData();
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
    public ListEvents getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, String words, boolean isAll) {
        boolean isAllLang = false;
        if (words==null) words = "";
        if (priceDown==null) priceDown = -1;
        if (priceUp==null) priceUp = -1;
        if (category==null) category = -1;
        if (language==null) language = -1;
        if (priceUp == -1) priceUp = Integer.MAX_VALUE;
        if (language == -1) isAllLang = true;
        String[] wds = words.split(",");
        long curr1 = System.currentTimeMillis();
        List<Event> list = getAll();//getEventRepository().getByFilter(priceUp, priceDown,category,language,isAll);
        long curr2 = System.currentTimeMillis();
        ListEvents copy = new ListEvents();
        for (Event aList : list) {
            if (((aList.getCategory() == category) || (category == -1)) && ((aList.getLanguage() == language) || isAllLang) && (aList.getType() == Consts.EXCURSION_ACTIVE || isAll)) {
                if ((aList.getPrice() >= priceDown) && (aList.getPrice() <= priceUp)) {

                    Photo img = photoService.getByEventId(aList.getId());
                    if (img != null)
                        aList.pathToPhoto = img.getData();

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
                if (aList.getPrice()>copy.getMaxPrice())
                    copy.setMaxPrice(aList.getPrice());
                if (aList.getPrice()<copy.getMinPrice())
                    copy.setMinPrice(aList.getPrice());
            }
        }
        long curr3 = System.currentTimeMillis();
        if (!words.equals("")) copy.sort((o1, o2) -> Integer.compare(o2.cnt, o1.cnt));
        logger.info("TIME11:"+(curr2-curr1));
        logger.info("TIME22:"+(curr3-curr2));
        return copy;
    }

    @Override
    public List<Event> getByGuideId(long guideId) {
        List<Event> eventList = new ArrayList<>();
        List<Event> copy = getAll();
        for (Event event:copy) {
            if (event.getGuideId()==guideId) {
                Photo img = photoService.getByEventId(event.getId());
                if (img != null)
                    event.pathToPhoto = img.getData();
                eventList.add(event);
            }
        }
        return eventList;
    }

    @Override
    public void setRate(int rate, long eventId) {
        Event e = getById(eventId);
        if (e!=null) {
            e.setRate((e.getRate() * e.getReviewsCount()) / (e.getReviewsCount() + 1));
            e.setReviewsCount(e.getReviewsCount() + 1);
            editEvent(e);
        }
    }

    ListEvents getByFilter(Integer priceUp, Integer priceDown, Integer category, Integer language, String words, Integer sortBy, boolean isAll) {
        ListEvents events = getByFilter(priceUp, priceDown, category, language, words, isAll);
        if (sortBy==null) return events;
        events.sort((o1, o2) -> {
            switch (sortBy) {
                case 0: return Long.compare(o1.getId(), o2.getId());
                case 1: return Long.compare(o2.getId(), o1.getId());
                case 2: return Integer.compare(o1.getPrice(), o2.getPrice());
                case 3: return Integer.compare(o2.getPrice(), o1.getPrice());
                case 4: return Float.compare(o1.getRate(), o2.getRate());
                case 5: return Float.compare(o2.getRate(), o1.getRate());
                case 6: return Integer.compare(o1.getType(), o2.getType());
                case 7: return Integer.compare(o2.getType(), o1.getType());
                case 8: return Integer.compare(o1.getDuration(), o2.getDuration());
                case 9: return Integer.compare(o2.getDuration(), o1.getDuration());
                case 10: return Integer.compare(o1.getUsersCount(), o2.getUsersCount());
                case 11: return Integer.compare(o2.getUsersCount(), o1.getUsersCount());
            }
            return Long.compare(o1.getId(), o2.getId());
        });
        return events;
    }
}
