package com.heroku.demo.event;

import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.review.ReviewServiceImpl;
import com.heroku.demo.utils.MessageUtil;
import com.heroku.demo.utils.Utils;
import com.heroku.demo.utils.UtilsForWeb;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.heroku.demo.utils.Utils.*;

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
    }

    @RequestMapping(value = "/add")
    public String eventAdd(ModelMap model) {
        model.addAttribute("inputEvent", new Event());
        return "event_add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              BindingResult result,
                              @RequestParam("file") MultipartFile file,
                              ModelMap modelMap, Principal principal, Locale locale) {
        String time = new LocalTime().toDateTimeToday().toString().replace('T', ' ');
        time = time.substring(0,time.indexOf('.'));
        event.setTime(time);
        event.setType(-3);
        String loginOrEmail = principal.getName();
        if (!loginOrEmail.equals("")) {
            Person p = personService.getByLoginOrEmail(loginOrEmail);
            event.setFullNameOfGuide(p.getFullName());
            event.setCity(p.getCity());
            event.setPhotoOfGuide(p.getImageToken());
            event.setGuideId(p.getId());
        } else event.setGuideId(-1);
        if (!result.hasErrors()) {
            eventService.addEvent(event);
        } else {
            modelMap.addAttribute("file", file);
            modelMap.addAttribute("inputEvent", event);
            modelMap.addAttribute("message", new MessageUtil("danger", messageSource.getMessage("error.event.add", null, locale)));
            return "event_add";
        }

        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                String fileName = file.getOriginalFilename();

                // Create the file on server
                File serverFile = new File(fileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                if (getFileSizeMegaBytes(serverFile) > 1)
                    serverFile = compress(serverFile, getFileExtension(fileName), getFileSizeMegaBytes(serverFile));

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());

                String photoToken = randomToken(32) + ".jpg";
                putImg(serverFile.getAbsolutePath(), photoToken);
                photoRepository.save(new Photo((int) event.getId(), photoToken));//todo
            } catch (Exception e) {
                logger.error("You failed to upload file => " + e.getMessage());
                eventService.delete(event.getId());
                return eventAddAgain(modelMap, event, "You failed to upload file. Please, try again.");
            }
            return event(modelMap, (int) event.getId());
        } else if (file == null) {
            return eventAddAgain(modelMap, event, "You failed to upload file because the file is null.");
        } else {
            return eventAddAgain(modelMap, event, "You failed to upload file because the file is empty.");
        }
    }

    private String eventAddAgain(ModelMap model, Event event, String errorData) {
        model.addAttribute("inputEvent", event);
        model.addAttribute("error_data", errorData);
        return "event_add";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteEvent(ModelMap model, @ModelAttribute("id") Long id,
                              @RequestParam(value = "price_up", required = false) Integer priceUp,
                              @RequestParam(value = "price_down", required = false) Integer priceDown,
                              @RequestParam(value = "category", required = false) Integer category,
                              @RequestParam(value = "words", required = false) String words,
                              Locale locale) {
        eventService.delete(id);
        return events(model, null, priceUp, priceDown, category, words, locale);
    }

    @RequestMapping(value = "/event", method = RequestMethod.GET)
    public String event(ModelMap model, @RequestParam("id") int id) {
        model.addAttribute("event", eventService.getById(id));
        model.addAttribute("reviews", reviewService.getByEvent(id));
        return "event";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String eventsTests(ModelMap model,
                              @RequestParam(value = "category", required = false) Integer category,
                              @RequestParam(value = "price_up", required = false) Integer priceUp,
                              @RequestParam(value = "price_down", required = false) Integer priceDown,
                              @RequestParam(value = "price", required = false) String price,
                              @RequestParam(value = "sort_by", required = false) Integer sortBy,
                              @RequestParam(value = "words", required = false) String words,
                              @RequestParam(value = "page", required = false) Integer page,
                              Locale locale) {
        if (price != null) {
            String prices[] = price.split(";");
            priceDown = Integer.parseInt(prices[0]);
            priceUp = Integer.parseInt(prices[1]);
        }
        ListEvents events = eventService.getByFilter(priceUp, priceDown, category, locale.getLanguage().equals("ru") ? 0 : 1, words, sortBy == null ? 0 : sortBy, false);//TODO optimize
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
        model.addAttribute("utils", new UtilsForWeb());
        return "event_list";
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/categories")
    @ResponseBody
    public String preview(@ModelAttribute("language") int language) {
        String ru = "[\"Развлечения\",\"Наука\",\"История\",\"Искусство\",\"Производство\",\"Гастрономия\",\"Квесты\",\"Экстрим\"]";
        String en = "[\"Entertainment\",\"Science\",\"History\",\"Art\",\"Manufacture\",\"Gastronomy\",\"Quests\",\"Extreme\"]";
        return language == 0 ? ru : en;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/listjson", method = RequestMethod.POST)
    @ResponseBody
    public String getEventsByFilter(@RequestParam(value = "price_down", required = false) Integer priceDown,
                                                    @RequestParam(value = "price_up", required = false) Integer priceUp,
                                                    @RequestParam(value = "category", required = false) Integer category,
                                                    @RequestParam(value = "words", required = false) String words,
                                                    @RequestParam(value = "language", required = false) Integer language,
                                                    @RequestParam(value = "sort_by", required = false) Integer sortBy,
                                                    @RequestParam(value = "auth") String authKey) {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words, sortBy, false);
        for (Event e : events) {
            arrayList.add(e.toString());
        }
        return Utils.list("events", arrayList, authKey, AUTH_KEY);
    }

    @RequestMapping("/moderation")
    public String events(ModelMap model,
                         @RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "price_up", required = false) Integer priceUp,
                         @RequestParam(value = "price_down", required = false) Integer priceDown,
                         @RequestParam(value = "category", required = false) Integer category,
                         @RequestParam(value = "words", required = false) String words,
                         Locale locale) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, locale.getLanguage().equals("ru") ? 0 : 1, words, null, true);
        model.addAttribute("events", events);

        if (id != null) {
            Event editEvent = eventService.getById(id);
            Photo img = photoService.getByEventId(id);
            if (img != null)
                editEvent.pathToPhoto = img.getData();
//            logger.info("EVENT PATH: "+editEvent.pathToPhoto);
            model.addAttribute("inputEvent", editEvent);
        } else model.addAttribute("inputEvent", new Event());

        return "events";
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.POST)
    public String insertModeration(@ModelAttribute("type") int type,
                                   @ModelAttribute("name") String name,
                                   @ModelAttribute("id") long id,
                                   @ModelAttribute("description") String description,
                                   @ModelAttribute("place") String place,
                                   @ModelAttribute("category") int category,
                                   @ModelAttribute("language") int language,
                                   ModelMap modelMap, Locale locale) {
        Event event1 = eventService.getById(id);
        event1.setType(type);
        event1.setCategory(category);
        event1.setName(name);
        event1.setDescription(description);
        event1.setPlace(place);
        event1.setLanguage(language);
        eventService.editEvent(event1);
        List<Event> events = eventService.getByFilter(null, null, null, locale.getLanguage().equals("ru") ? 0 : 1, null, null, true);
        modelMap.addAttribute("events", events);
        modelMap.addAttribute("inputEvent", event1);
        return "events";
    }

    @RequestMapping(value = "/addevent", method = RequestMethod.POST)
    @ResponseBody
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              BindingResult result,
                              @ModelAttribute("photo_azure") String photo,
                              @ModelAttribute("auth") String authKey) {
        if (authKey.equals(AUTH_KEY) && !result.hasErrors()) {//todo
            //person.setWhat(3);
            eventService.addEvent(event);
            if (photo != null)
                photoService.addPhoto(new Photo((int) event.getId(), photo));
        } else return "{}";
        return event.toString();
    }

    @RequestMapping("/updatedb")
    @ResponseBody
    public String updateDBEvents() {
        List<Event> events = eventService.getAll();
        for (Event event : events) {
            event.setGuideId(new Random().nextInt(3) + 37);
            //eventService.editEvent(event);
        }
        return "YES";
    }

}
