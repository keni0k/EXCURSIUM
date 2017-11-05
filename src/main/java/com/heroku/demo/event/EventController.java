package com.heroku.demo.event;

import com.heroku.demo.HomeController;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.utils.MessageUtil;
import com.heroku.demo.utils.UtilsForWeb;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.heroku.demo.utils.Utils.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private static String AUTH_KEY = "jP7xwaj12EYohNabYr1r6ewMeVJa8Jkf";

    private ReviewRepository reviewRepository;
    private PhotoRepository photoRepository;

    private EventServiceImpl eventService;
    private PhotoServiceImpl photoService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public EventController(PersonRepository personRepository, ReviewRepository reviewRepository,
                           EventRepository eventRepository, PhotoRepository photoRepository) {

        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;

        photoService = new PhotoServiceImpl(photoRepository);
        eventService = new EventServiceImpl(eventRepository, new PersonServiceImpl(personRepository), photoService);

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String eventAdd(ModelMap model) {
        model.addAttribute("inputEvent", new Event());
        return "event_add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              BindingResult result,
                              @RequestParam("file") MultipartFile file,
                              ModelMap modelMap) {
        event.setTime(new LocalTime().toDateTimeToday().toString());
        event.setGuideId(-1);
        if (!result.hasErrors()) {
            eventService.editEvent(event);
        } else {
            modelMap.addAttribute("file", file);
            modelMap.addAttribute("insertEvent", event);
            modelMap.addAttribute("message", new MessageUtil("danger", "During the addition of the tour any errors. Fix them to add it."));
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
                              @RequestParam(value = "language", required = false) Integer language,
                              @RequestParam(value = "words", required = false) String words) {
        eventService.delete(id);
        return events(model, null, priceUp, priceDown, category, language, words, 0);
    }

    @RequestMapping(value = "/event", method = RequestMethod.GET)
    public String event(ModelMap model, @RequestParam("id") int id) {
        model.addAttribute("event", eventService.getById(id));
        return "event";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String eventsByFilter(ModelMap model,
                                 @RequestParam(value = "category", required = false) Integer category,
                                 @RequestParam(value = "language", required = false) Integer language,
                                 @RequestParam(value = "price_up", required = false) Integer priceUp,
                                 @RequestParam(value = "price_down", required = false) Integer priceDown,
                                 @RequestParam(value = "sort_by", required = false) Integer sortBy,
                                 @RequestParam(value = "words", required = false) String words) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language==null?0:language, words, sortBy==null?0:sortBy);
        int size = events.size() % 3;
        for (int i = 0; i < size; i++) {
            events.remove(events.size() - 1);
        }
        model.addAttribute("events", events);
        model.addAttribute("utils", new UtilsForWeb());
        return "event_list";
    }

    @RequestMapping("/categories")
    @ResponseBody
    public ResponseEntity<String> preview(@ModelAttribute("language") int language) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        String ru = "[\"Развлечения\",\"Наука\",\"История\",\"Искусство\",\"Производство\",\"Гастрономия\",\"Квесты\",\"Экстрим\"]";
        String en = "[\"Entertainment\",\"Science\",\"History\",\"Art\",\"Manufacture\",\"Gastronomy\",\"Quests\",\"Extreme\"]";
        return new ResponseEntity<>(language == 0 ? ru : en, h, HttpStatus.OK);
    }


    @RequestMapping("/listjson")
    @ResponseBody
    public ResponseEntity<String> getEventsByFilter(@RequestParam(value = "price_down", required = false) Integer priceDown,
                                                    @RequestParam(value = "price_up", required = false) Integer priceUp,
                                                    @RequestParam(value = "category", required = false) Integer category,
                                                    @RequestParam(value = "words", required = false) String words,
                                                    @RequestParam(value = "language", required = false) Integer language,
                                                    @RequestParam(value = "sort_by", required = false) Integer sortBy,
                                                    @RequestParam(value = "auth") String authKey){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words, sortBy);
        for (Event e : events) {
            arrayList.add(e.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"events\": [");
        if (authKey.equals(AUTH_KEY)) {
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append(arrayList.get(i));
                if (arrayList.size() - i > 1) stringBuilder.append(",\n");
            }
        }
        stringBuilder.append("]}");
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        return new ResponseEntity<String>(stringBuilder.toString(), h, HttpStatus.OK);
    }

    @RequestMapping("/moderation")
    public String events(ModelMap model,
                         @RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "price_up", required = false) Integer priceUp,
                         @RequestParam(value = "price_down", required = false) Integer priceDown,
                         @RequestParam(value = "category", required = false) Integer category,
                         @RequestParam(value = "language", required = false) Integer language,
                         @RequestParam(value = "words", required = false) String words,
                         @RequestParam(value = "sort_by", required = false) Integer sortBy) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words, sortBy);
        model.addAttribute("events", events);

        if (id != null) {
            Event editEvent = eventService.getById(id);
            model.addAttribute("insertEvent", editEvent);
        } else model.addAttribute("insertEvent", new Event());

        if (sortBy == null) model.addAttribute("inc", 0);
        else if (sortBy % 2 == 0) model.addAttribute("inc", 1);
        else model.addAttribute("inc", 0);
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
            event.setGuideId(new Random().nextInt(3)+37);
            eventService.editEvent(event);
        }
        return "YES";
    }

}
