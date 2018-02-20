package com.heroku.demo.event;

import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.review.ReviewServiceImpl;
import com.heroku.demo.utils.*;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.heroku.demo.utils.Utils.localeToLang;

@Controller
@RequestMapping("/events")
public class EventController {

    private static String AUTH_KEY = "jP7xwaj12EYohNabYr1r6ewMeVJa8Jkf";
    private final MessageSource messageSource;

    private PhotoRepository photoRepository;
    private ReviewServiceImpl reviewService;

    private EventServiceImpl eventService;
    private PhotoServiceImpl photoService;
    private PersonServiceImpl personService;
    private Utils utils;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    public EventController(PersonRepository personRepository, ReviewRepository reviewRepository,
                           EventRepository eventRepository, PhotoRepository photoRepository, MessageSource messageSource) {

        personService = new PersonServiceImpl(personRepository, eventRepository, reviewRepository, photoRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
        this.photoRepository = photoRepository;
        photoService = new PhotoServiceImpl(photoRepository);
        eventService = new EventServiceImpl(eventRepository, photoService);
        this.messageSource = messageSource;
        this.utils = new Utils(personService);
    }

    @RequestMapping(value = "/add")
    public String eventAdd(ModelMap model, Principal principal) {
        model.addAttribute("inputEvent", new Event());
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("photos", null);
        model.addAttribute("errors", new Errors(false));
        model.addAttribute("person", utils.getPerson(principal));
        return "event/event_add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              @RequestParam(value = "photos") String photoTokens,
                              @RequestParam(value = "city_and_country", required = false) String cityAndCountry,
                              ModelMap modelMap, Principal principal, Locale locale) {
        event.setCountryAndCity(cityAndCountry);
        String time = new LocalTime().toDateTimeToday().toString().replace('T', ' ');
        time = time.substring(0,time.indexOf('.'));
        event.setTime(time);
        event.setType(Consts.EXCURSION_MODERATION);
        String[] tokens = photoTokens.split(";");
        setSmallData(event);
        Errors errors = findErrors(event);

        Person person = utils.getPerson(principal);
        if (person!=null) {
            event.setFullNameOfGuide(person.getFullName());
            event.setPhotoOfGuide(person.getImageToken());
            event.setGuideId(person.getId());
        } else {
            return "redirect:/users/login";
        }

        eventService.addEvent(event);
        logger.info("ID1: "+event.getId());
        for (int i = 0; i<tokens.length; i++) {
            Photo photo = photoService.getByToken(tokens[i]);
            if (photo != null) {
                photo.setEventId(event.getId());
                photo.setNumber(i);
                photoService.editPhoto(photo);
            }
        }
        if (errors.isErrors()) return eventAddAgain(modelMap, event, new MessageUtil("warning", messageSource.getMessage("error.event.add", null, locale)), principal, errors);
        return "redirect:/events/event?id="+event.getId();
    }

    @RequestMapping(value = "/edit")
    public String eventEdit(ModelMap model, Principal principal, @RequestParam(value = "id") long id){
        Person person = utils.getPerson(principal);
        if (person!=null) {
            Event event = eventService.getById(id);
            if (event.getGuideId()==person.getId())
                return eventAddAgain(model, event, null, principal, findErrors(event));
        }
        return "redirect:/users/login";
    }

    private String eventAddAgain(ModelMap model, Event event, MessageUtil message, Principal principal, Errors errors) {
        model.addAttribute("inputEvent", event);
        model.addAttribute("photos", photoService.getByEventId(event.getId()));
        model.addAttribute("errors", errors);
        model.addAttribute("message", message);
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("person", utils.getPerson(principal));
        return "event/event_add";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteEvent(ModelMap model, @ModelAttribute("id") Long id,
                              @RequestParam(value = "price_up", required = false) Integer priceUp,
                              @RequestParam(value = "price_down", required = false) Integer priceDown,
                              @RequestParam(value = "category", required = false) Integer category,
                              @RequestParam(value = "words", required = false) String words,
                              Locale locale, Principal principal) {
        Event e = eventService.getById(id);
        e.setType(Consts.EXCURSION_DELETED);
        eventService.editEvent(e);
        return events(model, null, priceUp, priceDown, category, null, null, words, locale, principal);
    }

    @RequestMapping(value = "/event", method = RequestMethod.GET)
    public String event(ModelMap model, @RequestParam("id") int id, Principal principal) {
        model.addAttribute("event", eventService.getById(id));
        model.addAttribute("reviews", reviewService.getByEvent(id));
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("person", utils.getPerson(principal));
        return "event/event";
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public String cities(ModelMap model, @RequestParam(value = "country", required = false) Integer country, Principal principal) {

        if (country==null) return countries(model, principal);

        //model.addAttribute("utils", new UtilsForWeb());
//        model.addAttribute("items", );
        model.addAttribute("type", 1);
        model.addAttribute("country", country);
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("service", eventService);
        model.addAttribute("person", utils.getPerson(principal));

        return "event/countries_and_cities";
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public String countries(ModelMap model, Principal principal) {
        //model.addAttribute("utils", new UtilsForWeb());

//        model.addAttribute("items", );
        model.addAttribute("type", 0);
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("service", eventService);
        model.addAttribute("person", utils.getPerson(principal));

        return "event/countries_and_cities";
    }

    @RequestMapping(value = "/list_test", method = RequestMethod.GET)
    public String eventsTests(ModelMap model,
                              @RequestParam(value = "category", required = false) Integer category,
                              @RequestParam(value = "price_up", required = false) Integer priceUp,
                              @RequestParam(value = "price_down", required = false) Integer priceDown,
                              @RequestParam(value = "price", required = false) String price,
                              @RequestParam(value = "sort_by", required = false) Integer sortBy,
                              @RequestParam(value = "words", required = false) String words,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "country", required = false) Integer country,
                              @RequestParam(value = "city", required = false) Integer city,
                              Locale locale, Principal principal) {
        if (country==null) return countries(model, principal);
        if (city==null) return cities(model, country, principal);
        if (price != null) {
            String prices[] = price.split(";");
            priceDown = Integer.parseInt(prices[0]);
            priceUp = Integer.parseInt(prices[1]);
        }
        ListEvents events = eventService.getByFilter(priceUp, priceDown, category, localeToLang(locale), country, city, words, sortBy == null ? 0 : sortBy, false);//TODO optimize
        ListEvents eventsFinal = new ListEvents();
        eventsFinal.setMinPrice(events.getMinPrice());
        eventsFinal.setMaxPrice(events.getMaxPrice());
        if (page==null) page = 1;
        int cards = (page-1) * 12;
        for (int i = cards; i < cards + 12; i++)
            if (i < events.size())
                eventsFinal.add(events.get(i));
        int pageCount = (int)(Math.ceil((double)events.size() / 12));

        if (eventsFinal.size()>0) events = eventsFinal;//Если страница не пуста, то показать ее, иначе показать все экскурсии

        if (priceDown==null || priceDown<events.getMinPrice())
            priceDown=events.getMinPrice();//Если нет ограничений по цене, то выставить минимальную и максимальную цены
        if (priceUp==null || priceDown>events.getMaxPrice())
            priceUp=events.getMaxPrice();
        if (priceDown >= events.getMaxPrice()) priceDown=events.getMaxPrice()-40;

        model.addAttribute("sort_by", sortBy);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("page", page);
        model.addAttribute("minPrice", events.getMinPrice());
        model.addAttribute("maxPrice", events.getMaxPrice());
        model.addAttribute("minMaxPrice", priceDown+";"+priceUp);
        model.addAttribute("events", events);
        model.addAttribute("category", category);
        model.addAttribute("country", country);
        model.addAttribute("city", city);
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("person", utils.getPerson(principal));

        return "event/event_list";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listTest(ModelMap model,
                           @RequestParam(value = "country", required = false) Integer country,
                           @RequestParam(value = "city", required = false) Integer city,
                           @RequestParam(value = "country_and_city", required = false) String countryAndCity,
                           Locale locale, Principal principal){
        if (countryAndCity!=null && countryAndCity.length()>0) {
            country = Integer.parseInt(countryAndCity.substring(0,1));
            city = Integer.parseInt(countryAndCity.substring(2,3));
        }
        if (country==null) return countries(model, principal);
        if (city==null) return cities(model, country, principal);
        ListEvents events = eventService.getByFilter(null, null, null, localeToLang(locale), country, city, null, 0, false);
        model.addAttribute("minPrice", events.getMinPrice());
        model.addAttribute("maxPrice", events.getMaxPrice());
        model.addAttribute("events", events);
        model.addAttribute("country", country);
        model.addAttribute("city", city);
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("person", utils.getPerson(principal));
        return "event/index";
    }

    @RequestMapping("/categories")
    @ResponseBody
    public ResponseEntity<String> preview(@RequestParam(value = "language", required = false) Integer language) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        String ru = "[\"Развлечения\",\"Наука\",\"История\",\"Искусство\",\"Производство\",\"Гастрономия\",\"Квесты\",\"Экстрим\"]";
        String en = "[\"Entertainment\",\"Science\",\"History\",\"Art\",\"Manufacture\",\"Gastronomy\",\"Quests\",\"Extreme\"]";
        String categories;
        if (language==null) language = 0;
        switch (language){
            case 1: categories = en; break;
            default: categories = ru; break;
        }
        return new ResponseEntity<>(categories, h, HttpStatus.OK);
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getEventsByFilter(@RequestParam(value = "price_down", required = false) Integer priceDown,
                                                    @RequestParam(value = "price_up", required = false) Integer priceUp,
                                                    @RequestParam(value = "category", required = false) Integer category,
                                                    @RequestParam(value = "words", required = false) String words,
                                                    @RequestParam(value = "language", required = false) Integer language,
                                                    @RequestParam(value = "sort_by", required = false) Integer sortBy,
                                                    @RequestParam(value = "country", required = false) Integer country,
                                                    @RequestParam(value = "city", required = false) Integer city,
                                                    @RequestParam(value = "auth") String authKey) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, country, city, words, sortBy, false);
        for (Event e : events) {
            arrayList.add(e.toString());
        }
        return new ResponseEntity<>(Utils.list("events", arrayList, authKey, AUTH_KEY), h, HttpStatus.OK);
    }

    @RequestMapping("/moderation")
    public String events(ModelMap model,
                         @RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "price_up", required = false) Integer priceUp,
                         @RequestParam(value = "price_down", required = false) Integer priceDown,
                         @RequestParam(value = "category", required = false) Integer category,
                         @RequestParam(value = "country", required = false) Integer country,
                         @RequestParam(value = "city", required = false) Integer city,
                         @RequestParam(value = "words", required = false) String words,
                         Locale locale, Principal principal) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, localeToLang(locale), country, city, words, null, true);
        model.addAttribute("events", events);

        if (id != null) {
            Event editEvent = eventService.getById(id);
            List<Photo> photos = photoService.getByEventId(editEvent.getId());
            if (photos != null)
                for (Photo p:photos) {
                    editEvent.pathToPhoto.add(p.getData());
                }
            model.addAttribute("inputEvent", editEvent);
        }
        model.addAttribute("utils", new UtilsForWeb());
        model.addAttribute("person", utils.getPerson(principal));
        return "admin/events";
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.POST)
    public String insertModeration(@ModelAttribute("type") int type,
                                   @ModelAttribute("name") String name,
                                   @ModelAttribute("id") long id,
                                   @ModelAttribute("description") String description,
                                   @ModelAttribute("place") String place,
                                   @ModelAttribute("category") int category,
                                   @ModelAttribute("language") int language,
                                   ModelMap modelMap, Locale locale, Principal principal) {
        Event event1 = eventService.getById(id);
        event1.setType(type);
        event1.setCategory(category);
        event1.setName(name);
        event1.setDescription(description);
        event1.setPlace(place);
        event1.setLanguage(language);
        eventService.editEvent(event1);
        List<Event> events = eventService.getByFilter(null, null, null, localeToLang(locale), null, null, null, null, true);
        modelMap.addAttribute("events", events);
        modelMap.addAttribute("inputEvent", event1);
        modelMap.addAttribute("utils", new UtilsForWeb());
        modelMap.addAttribute("person", utils.getPerson(principal));
        return "admin/events";
    }

    @RequestMapping(value = "/addevent", method = RequestMethod.POST)
    @ResponseBody
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              BindingResult result,
                              @ModelAttribute("photo_azure") String photo,
                              @ModelAttribute("auth") String authKey) {
        if (authKey.equals(AUTH_KEY) && !result.hasErrors()) {//todo
            event.setType(Consts.EXCURSION_MODERATION);
            eventService.addEvent(event);
            if (photo != null)
                photoService.addPhoto(new Photo(event.getId(), photo, 0));
        } else return "{}";
        return event.toString();
    }

    @RequestMapping("/updatedb")
    @ResponseBody
    public String updateDBEvents() {
        List<Event> events = eventService.getAll();
        Random random = new Random();
        for (Event event: events) {
            int i;
            int type = event.getTypeOfDates();
            event.setActiveDates("");
            if (type==0){
                for (i = 0; i<7; i++) {
                    if (random.nextInt(2) == 0) {
                        if (i != 6)
                            event.setActiveDates(event.getActiveDates() + ';');
                    } else {
                        String suffix = i != 6 ? ";" : "";
                        event.setActiveDates(event.getActiveDates() + random.nextInt(48) + '-' + random.nextInt(48) + suffix);
                    }
                }
            }
            if (type==1){
                for (i = 0; i<7; i++) {
                    if (random.nextInt(3) != 0)
                        addTime(random, event, random.nextInt(8));
                    if (i!=6)
                        event.setActiveDates(event.getActiveDates() + ';');
                }
            }
            if (type==2){
                int dates = random.nextInt(5);
                for (i = 0; i<dates; i++){
                    String date1 = "" + random.nextInt(3) + random.nextInt(10) + ".0" + (random.nextInt(9)+1) + ".2018";
                    event.setActiveDates(event.getActiveDates() + date1+ ":");
                    int times = random.nextInt(5);
                    addTime(random, event, times);
                    if (i!=dates-1)
                        event.setActiveDates(event.getActiveDates() + ";");
                }
            }

            eventService.editEvent(event);
        }
        return "Yes";
    }

    private void addTime(Random random, Event event, int times) {
        for (int j = 0; j<times; j++){
            String suffix = j != times-1 ? "," : "";
            event.setActiveDates(event.getActiveDates()+random.nextInt(48)+suffix);
        }
    }

    private void setSmallData(Event event) {
        String txt = event.getDescription();
        if (txt.length() >= 150) {
            int i;
            for (i = 1; i < 149; i++) {
                String substr = txt.substring(149 - i, 149);
                if (substr.indexOf('.') != -1 || substr.indexOf('!') != -1 || substr.indexOf('?') != -1) break;
            }
            if (i != 149 && i < 70) event.setSmallData(txt.substring(0, 149 - i + 1));
            else event.setSmallData(txt.substring(0, 148) + '…');
        }
    }

    private Errors findErrors(Event event){
        boolean isN = event.getName().length()<10||event.getName().length()>60;
        boolean isDesc = event.getDescription().length()<150;
        boolean isPlace = event.getPlace().length()<5||event.getPlace().length()>100;
        boolean isPrice = event.getPrice()<100||event.getPrice()>100000;
        boolean isDur = event.getDuration()<1||event.getDuration()>240;
        boolean isUC = event.getUsersCount()<1||event.getUsersCount()>100;
        return new Errors(isN, isDesc, isPlace, isPrice, isDur, isUC);
    }
}
