package com.heroku.demo;

import com.heroku.demo.event.Event;
import com.heroku.demo.event.EventRepository;
import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.message.Message;
import com.heroku.demo.message.MessageRepository;
import com.heroku.demo.order.Buy;
import com.heroku.demo.order.BuyRepository;
import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.review.Review;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.utils.UtilsForWeb;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.Email;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/")
public class HomeController {

    private MessageRepository messageRepository;
    private ReviewRepository reviewRepository;
    private BuyRepository buyRepository;
    private PhotoRepository photoRepository;

    private PersonServiceImpl personService;
    private EventServiceImpl eventService;
    private PhotoServiceImpl photoService;

    private LocalTime localTime;
    private static final Logger logger = LoggerFactory
            .getLogger(HomeController.class);

    @Autowired
    public HomeController(PersonRepository personRepository, MessageRepository pRepository,
                          ReviewRepository reviewRepository, EventRepository eventRepository,
                          BuyRepository buyRepository, PhotoRepository photoRepository) {

        this.buyRepository = buyRepository;
        this.messageRepository = pRepository;
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;

        photoService = new PhotoServiceImpl(photoRepository);
        personService = new PersonServiceImpl(personRepository);
        eventService = new EventServiceImpl(eventRepository, personService, photoService);

    }

    @RequestMapping("upload")
    public String upload(ModelMap model) {
        return "upload";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping("index")
    public String index2(ModelMap model) {
        return "index";
    }

    @RequestMapping("registration")
    public String persons(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "registration";
    }

    @RequestMapping("/event_add")
    public String eventAdd(ModelMap model) {
        model.addAttribute("inputEvent", new Event());
        return "event_add";
    }

    @RequestMapping("/event")
    public String event(ModelMap model, @ModelAttribute("id") int id) {
        model.addAttribute("event", eventService.getById(id));
        return "event";
    }

    @RequestMapping("/event_list")
    public String eventList(ModelMap model) {
        List<Event> events = eventService.getByFilter(-1, -1, -1, 0, "");
        int size = events.size() % 3;
        for (int i = 0; i < size; i++) {
            events.remove(events.size() - 1);
        }
        model.addAttribute("events", events);
        model.addAttribute("utils", new UtilsForWeb());
        return "event_list";
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getperson")
    @ResponseBody
    public String getPerson(ModelMap model,
                            @ModelAttribute("token") String token,
                            BindingResult result) {
        Person p = personService.getByToken(token);
        return p == null ? "{}" : p.toString();
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getpersonbyemail")
    @ResponseBody
    public String getPersonByEmail(ModelMap model,
                                   @ModelAttribute("email") String email,
                                   BindingResult result) {
        Person p = personService.getByEmail(email);
        return p == null ? "{}" : p.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getcategories")
    @ResponseBody
    public ResponseEntity<String> preview(HttpServletResponse response, @ModelAttribute("language") int language) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        String ru = "[\"Развлечения\",\"Наука\",\"История\",\"Искусство\",\"Производство\",\"Гастрономия\",\"Квесты\",\"Экстрим\"]";
        String en = "[\"Entertainment\",\"Science\",\"History\",\"Art\",\"Manufacture\",\"Gastronomy\",\"Quests\",\"Extreme\"]";
        return new ResponseEntity<>(language == 0 ? ru : en, h, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getexcursions")
    @ResponseBody
    public ResponseEntity<String> getEventsByFilter(ModelMap model,
                                                    @ModelAttribute("price_down") int priceDown,
                                                    @ModelAttribute("price_up") int priceUp,
                                                    @ModelAttribute("category") int category,
                                                    @ModelAttribute("words") String words,
                                                    @ModelAttribute("language") int language) {

        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words);
        for (Event e : events) {
            arrayList.add(e.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"events\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        return new ResponseEntity<String>(stringBuilder.toString(), h, HttpStatus.OK);
    }


    private String randomToken(int length) {
        final String mCHAR = "qwertyuioplkjhgfdsazxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(mCHAR.length());
            char ch = mCHAR.charAt(number);
            builder.append(ch);
        }
        return builder.toString();
    }
    @RequestMapping("/sendemail")
    @ResponseBody
    private String sendMail() throws MailjetSocketTimeoutException, MailjetException {
        MailjetRequest email;
        JSONArray recipients;
        MailjetResponse response;
        MailjetClient client = new MailjetClient(System.getenv("MJ_APIKEY_PUBLIC"), System.getenv("MJ_APIKEY_PRIVATE"));

        recipients = new JSONArray()
                .put(new JSONObject().put(Contact.EMAIL, "dima-vers0@rambler.ru"));

        email = new MailjetRequest(Email.resource)
                .property(Email.FROMNAME, "Excursium")
                .property(Email.FROMEMAIL, "support@excursium.me")
                .property(Email.SUBJECT, "Subject")
                .property(Email.TEXTPART, "Java is coming!...")
                .property(Email.RECIPIENTS, recipients)
                .property(Email.MJCUSTOMID, "JAVA-Email");

        response = client.post(email);
        return response.getData() + " " + response.getStatus();
    }

    @RequestMapping("/addpersonhttp")
    public String insertContact(ModelMap model,
                                @ModelAttribute("insertPerson") @Valid Person person,
                                BindingResult result) {
        person.setToken(randomToken(32));

        if (!personService.throwsErrors(person)) {
            String errorData = "";
            if (!personService.isEmailFree(person.getEmail()))
                errorData = errorData.concat("EMAIL IS NOT FREE\n");
            if (!personService.isLoginFree(person.getLogin()))
                errorData = errorData.concat("LOGIN IS NOT FREE\n");
            if (!personService.isEmailCorrect(person.getEmail()))
                errorData = errorData.concat("EMAIL IS NOT VALID\n");
            if (!personService.isPhoneFree(person.getPhoneNumber()))
                errorData = errorData.concat("PHONE NUMBER IS NOT FREE");
            model.addAttribute("error_data", errorData);
            return "error";
        }
        if (!result.hasErrors()) {
            // person.setWhat(2);
            personService.addPerson(person);
        }
        return persons(model);
    }

    @RequestMapping("/events")
    public String events(ModelMap model) {
        List<Event> events = eventService.getAll();
        model.addAttribute("events", events);
        model.addAttribute("insertEvent", new Event());
        return "events";
    }

    @RequestMapping("/updatedbevents")
    @ResponseBody
    public String updateDBEvents() {
        List<Event> events = eventService.getAll();
        for (Event event :
                events) {
            //DOIT
            //eventService.editEvent(event);
        }
        return "YES";
    }

    @RequestMapping("/updatedbpersons")
    @ResponseBody
    public String updateDBPersons() {
        List<Person> persons = personService.getAll();
        for (Person person : persons) {
            //DOIT
            //personService.editPerson(person);
        }
        return "YES";
    }

    @RequestMapping(value = "/event_list", params = "category")
    public String eventsByCategory(ModelMap model,
                                   @ModelAttribute("category") int category) {
        List<Event> events = eventService.getByFilter(-1, -1, category, 0, "");
        int size = events.size() % 3;
        for (int i = 0; i < size; i++) {
            events.remove(events.size() - 1);
        }
        model.addAttribute("events", events);
        model.addAttribute("utils", new UtilsForWeb());
        return "event_list";
    }

    @RequestMapping(value = "/event_list", params = {"category", "language", "price_up", "price_down", "words"})
    public String eventsByFilter(ModelMap model,
                                 @ModelAttribute("category") int category,
                                 @ModelAttribute("language") int language,
                                 @ModelAttribute("price_up") int priceUp,
                                 @ModelAttribute("price_down") int priceDown,
                                 @ModelAttribute("words") String words) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words);
        int size = events.size() % 3;
        for (int i = 0; i < size; i++) {
            events.remove(events.size() - 1);
        }
        model.addAttribute("events", events);
        model.addAttribute("utils", new UtilsForWeb());
        return "event_list";
    }

    @RequestMapping("/persons")
    public String persons_last(ModelMap model) {
        List<Person> persons = personService.getAll();
        model.addAttribute("persons", persons);
        model.addAttribute("insertPerson", new Person());
        return "persons";
    }

    private void putImg(long eventId, String path) throws StorageException, URISyntaxException, IOException, InvalidKeyException {

        String photoToken = randomToken(32);
        photoRepository.save(new Photo((int)eventId, photoToken));//todo
        CloudStorageAccount account = CloudStorageAccount.parse("DefaultEndpointsProtocol=https;AccountName=excursium;AccountKey=fbMSD2cjYX08BJeKQvNM4Wk87I7fGWJShZvdtR3BdwvhXKUFuYv//qtJs9eAKmESG4Ib7CAHDJlgOIxSw5wwfg==;EndpointSuffix=core.windows.net");
        CloudBlobClient client = account.createCloudBlobClient();
        CloudBlobContainer container = client.getContainerReference("img");
        CloudBlockBlob blob1 = container.getBlockBlobReference(photoToken);
        blob1.uploadFromFile(path);
    }

    @RequestMapping(value = "/addeventhttp", method = RequestMethod.POST)
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              @RequestParam("file") MultipartFile file) {
        event.setTime(new LocalTime().toString());
        event.setGuideId(-1);
        event.setPhotoUrl("URL");

        if (eventService!=null) {
            eventService.addEvent(event);
        } else return "ERROR";

        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(file.getName());
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());

                putImg(event.getId(), serverFile.getAbsolutePath());
            } catch (Exception e) {
                logger.error("You failed to upload file => " + e.getMessage());
                return "ERROR UPLOAD FILE";
            }
            return "SUCCESFULL";
        } else if (file==null){
            return "You failed to upload file because the file is null.";
        }
        else {
            return "You failed to upload file because the file is empty.";
        }
        //eventAdd(model);
    }


    @RequestMapping("/deleteperson")
    public String deleteContact(@ModelAttribute("id") String id) {
        personService.delete(Long.parseLong(id));
        return persons_last(new ModelMap());
    }

    @RequestMapping("/deleteevent")
    public String deleteEvent(ModelMap model, @ModelAttribute("id") String id) {
        eventService.delete(Long.parseLong(id));
        return events(model);
    }

    @RequestMapping("/addmsg")
    @ResponseBody
    public String insertMsg(ModelMap model,
                            @ModelAttribute("msg") String message,
                            @ModelAttribute("event_id") int eventId,
                            @ModelAttribute("user_id") int userId,
                            BindingResult result) {
        Message msg = new Message(userId, eventId, new LocalTime().toDateTimeToday().toString(), message);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            messageRepository.save(msg);
        }
        return msg.toString();
    }

    @RequestMapping("/addperson")
    @ResponseBody
    public String insertPerson(ModelMap model,
                               @ModelAttribute("pass") String pass,
                               @ModelAttribute("login") String login,
                               @ModelAttribute("type") int type,
                               @ModelAttribute("last_name") String lastName,
                               @ModelAttribute("first_name") String firstName,
                               @ModelAttribute("phone_number") String phoneNumber,
                               @ModelAttribute("rate") int rate,
                               @ModelAttribute("city") String city,
                               @ModelAttribute("email") String email,
                               @ModelAttribute("about") String about,
                               @ModelAttribute("date") String date,
                               @ModelAttribute("imageurl") String imageUrl,
                               BindingResult result) {

        Person p = new Person(login, pass, lastName, type, email, firstName, rate, phoneNumber, about, city, date, imageUrl);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            personService.addPerson(p);
        }
        return p.toString();
    }


    @RequestMapping("/getpersons")
    @ResponseBody
    public String getPersons() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Person> persons = personService.getAll();

        StringBuilder stringBuilder = new StringBuilder("{ \"persons\": [");

        for (Person p : persons) {
            arrayList.add(p.toString());
        }

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("/getmsgs")
    @ResponseBody
    public String getMsgs() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Message> messages = messageRepository.findAll();
        for (Message m : messages) {
            arrayList.add(m.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"messages\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("/addevent")
    @ResponseBody
    public String insertEvent(ModelMap model,
                              @ModelAttribute("name") String name,
                              @ModelAttribute("category") int category,
                              @ModelAttribute("place") String place,
                              @ModelAttribute("duration") int duration,
                              @ModelAttribute("description") String description,
                              @ModelAttribute("language") int language,
                              @ModelAttribute("users_count") int usersCount,
                              @ModelAttribute("price") int price,
                              @ModelAttribute("photo") int photo,
                              BindingResult result) {

        Event e = new Event(place, category, "TIME", duration, price, description, -1, photo, name, -1, usersCount, language);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            eventService.addEvent(e);
        }
        return e.toString();
    }


    @RequestMapping("/getevents")
    @ResponseBody
    public String getEvents() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getAll();
        for (Event e : events) {
            arrayList.add(e.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"events\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("/addreview")
    @ResponseBody
    public String insertReview(ModelMap model,
                               @ModelAttribute("data") String data,
                               @ModelAttribute("image_url") String imageUrl,
                               @ModelAttribute("user_id") int userId,
                               @ModelAttribute("event_id") int eventId,
                               @ModelAttribute("rate") int rate,
                               BindingResult result) {

        Review review = new Review(data, imageUrl, "TIME", userId, eventId, rate);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            reviewRepository.save(review);
        }
        return review.toString();
    }


    @RequestMapping("/getreviews")
    @ResponseBody
    public String getReviews() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Review> persons = reviewRepository.findAll();
        for (Review r : persons) {
            arrayList.add(r.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"reviews\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping("/addorder")
    @ResponseBody
    public String insertOrder(ModelMap model,
                              @ModelAttribute("price") int price,
                              @ModelAttribute("tourist_id") int touristId,
                              @ModelAttribute("event_id") int eventId,
                              BindingResult result) {

        Buy buy = new Buy(eventId, touristId, price, "TIME");
        if (!result.hasErrors()) {
            //person.setWhat(3);
            buyRepository.save(buy);
        }
        return buy.toString();
    }


    @RequestMapping("/getorders")
    @ResponseBody
    public String getOrders() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Buy> buys = buyRepository.findAll();
        for (Buy o : buys) {
            arrayList.add(o.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"buys\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("/addphoto")
    @ResponseBody
    public String insertPhoto(ModelMap model,
                              @ModelAttribute("data") String data,
                              @ModelAttribute("event_id") int eventId,
                              BindingResult result) {

        Photo photo = new Photo(eventId, data);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            photoRepository.save(photo);
        }
        return photo.toString();
    }

    @RequestMapping("/getphotos")
    @ResponseBody
    public String getPhotos() {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Photo> buys = photoRepository.findAll();
        for (Photo o : buys) {
            arrayList.add(o.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"photos\": [");

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size() - i > 1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping("/profile")
    public String profile(ModelMap model, @ModelAttribute("id") long id) {
        Person p = personService.getById(id);
        model.addAttribute("person", p != null ? p : new Person());
        //model.addAttribute("insertEvent", new Event());
        return "profile";
    }

}
