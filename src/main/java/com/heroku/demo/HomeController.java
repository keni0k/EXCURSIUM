
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
import com.heroku.demo.review.Review;
import com.heroku.demo.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {


    private PersonRepository personRepository;
    private MessageRepository messageRepository;
    private EventRepository eventRepository;
    private ReviewRepository reviewRepository;
    private BuyRepository buyRepository;
    private PhotoRepository photoRepository;

    private PersonServiceImpl personService;
    private EventServiceImpl eventService;

    @Autowired
    public HomeController(PersonRepository repository, MessageRepository pRepository,
                          ReviewRepository reviewRepository, EventRepository eventRepository,
                          BuyRepository buyRepository, PhotoRepository photoRepository) {
        this.buyRepository = buyRepository;
        this.personRepository = repository;
        this.messageRepository = pRepository;
        this.eventRepository = eventRepository;
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;

        personService = new PersonServiceImpl(personRepository);
        eventService = new EventServiceImpl(eventRepository);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping("index")
    public String index2(ModelMap model) {
        return "index";
    }

    public String persons(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "registration";
    }

    @RequestMapping("/event_add")
    public String eventAdd(ModelMap model) {
        model.addAttribute("insertEvent", new Event());
        return "event_add";
    }

    @RequestMapping("/event")
    public String event(ModelMap model, @ModelAttribute("id") int id) {
        switch (id){
            case 74:
                model.addAttribute("photo", "../resources/img/doshik.jpg");break;
            case 78:
                model.addAttribute("photo", "../resources/img/poets.jpg");break;
            case 75:
                model.addAttribute("photo", "../resources/img/2007.jpg");break;
            case 77:
                model.addAttribute("photo", "../resources/img/speceffects.jpg");break;
        }
        model.addAttribute("event", eventService.getById(id));
        //model.addAttribute("insertEvent", new Event());
        return "event";
    }

    @RequestMapping("/event_list")
    public String eventList(ModelMap model) {
        List<Event> events = eventService.getByFilter(-1,-1,-1,0);
        String [] photos = {"../resources/img/metro.jpg","../resources/img/bezdomnie.jpg","../resources/img/kafedra_norm.jpg"
                , "../resources/img/evolution.jpg","../img/resources/img/podpolie.jpg",
                "../resources/img/vslepuu.jpg","../resources/img/chainaut.jpg","../resources/img/remote_moscow",
                "../resources/img/gastronom.jpg", "../resources/img/kontrasts.jpg","../img/resources/img/multymediaart.jpg",
                "../resources/img/keramika.jpg", "../resources/img/plarus.jpg","../img/resources/img/rot_front.jpg",
                "../resources/img/without_portfel.jpg"};
        for (int i = 0; i<photos.length; i++) {
            events.get(i).setPhotoUrl(photos[i]);
            eventService.editEvent(events.get(i));
        }
        model.addAttribute("events", events);
        return "event_list";
    }

    @RequestMapping("/profile")
    public String profile(ModelMap model, @ModelAttribute("id") int id) {
        switch (id){
            case 74:
                model.addAttribute("name", "Андрей");
                model.addAttribute("city", "Москва");
                model.addAttribute("about", "Я сделал сайт");break;
            case 78:
                model.addAttribute("name", "Леша");
                model.addAttribute("city", "Москва");
                model.addAttribute("about", "Я сделал telegram бота");break;
            case 75:
                model.addAttribute("name", "Миша");
                model.addAttribute("city", "Москва");
                model.addAttribute("about", "Я сделал презентацию");break;
            case 77:
                model.addAttribute("name", "Дима");
                model.addAttribute("city", "Иркутск");
                model.addAttribute("about", "Я сделал сервер и только я могу поменять описания Миши, Леши и Андрея");break;
        }
        //model.addAttribute("insertEvent", new Event());
        return "profile";
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getperson")
    @ResponseBody
    public String getPerson(ModelMap model,
                             @ModelAttribute("token") String token,
                             BindingResult result){
        Person p = personService.getByToken(token);
        return p==null?"{}":p.toString();
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getpersonbyemail")
    @ResponseBody
    public String getPersonByEmail(ModelMap model,
                            @ModelAttribute("email") String email,
                            BindingResult result){
        Person p = personService.getByEmail(email);
        return p==null?"{}":p.toString();
    }

   // @RequestMapping(method = RequestMethod.GET, value = "/getcategories")
   // @ResponseBody
  //  public String getCategories(){
  //      return "[\"Авто\",\"Пешеходная\",\"Квест\",\"Экстримальная\"]";
   // }

    @RequestMapping(method = RequestMethod.GET, value = "/getcategories")
    @ResponseBody
    public ResponseEntity<String> preview(HttpServletResponse response, @ModelAttribute("language")int language) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        String ru = "[\"Развлечения\",\"Наука\",\"История\",\"Искусство\",\"Производство\",\"Гастрономия\",\"Квесты\",\"Экстрим\"]";
        String en = "[\"Entertainment\",\"Science\",\"History\",\"Art\",\"Manufacture\",\"Gastronomy\",\"Quests\",\"Extreme\"]";
        return new ResponseEntity<>(language==0?ru:en,h , HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getexcursions")
    @ResponseBody
    public ResponseEntity<String> getEventsByFilter(ModelMap model,
                                   @ModelAttribute("price_down")int priceDown,
                                    @ModelAttribute("price_up")int priceUp,
                                    @ModelAttribute("category")int category,
                                                    @ModelAttribute("language")int language){

        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language);
        for (Event e:events){
            arrayList.add(e.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"events\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        return new ResponseEntity<String>(stringBuilder.toString(),h , HttpStatus.OK);
    }


    private String randomToken(){
        final String mCHAR = "qwertyuioplkjhgfdsazxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        final int STR_LENGTH = 32; // длина генерируемой строки

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < STR_LENGTH; i++) {
            int number = random.nextInt(mCHAR.length());
            char ch = mCHAR.charAt(number);
            builder.append(ch);
        }
        return builder.toString();
    }

    private void sendMail(){
//        final String APIKey = "your Mailjet API Key";
//        final String SecretKey = "your Mailjet Secret Key";
//        String From = "you@example.com";
//        String To = "recipient@example.com";
//
//        Properties props = new Properties ();
//
//        props.put ("mail.smtp.host", "in.mailjet.com");
//        props.put ("mail.smtp.socketFactory.port", "465");
//        props.put ("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put ("mail.smtp.auth", "true");
//        props.put ("mail.smtp.port", "465");
//
//        Session session = Session.getDefaultInstance (props,
//                new javax.mail.Authenticator ()
//                {
//                    protected PasswordAuthentication getPasswordAuthentication ()
//                    {
//                        return new PasswordAuthentication (APIKey, SecretKey);
//                    }
//                });
//
//        try
//        {
//
//            Message message = new MimeMessage (session);
//            message.setFrom (new InternetAddress (From));
//            message.setRecipients (Message.RecipientType.TO, InternetAddress.parse(To));
//            message.setSubject ("Your mail from Mailjet");
//            message.setText ("Your mail from Mailjet, sent by Java.");
//
//            Transport.send (message);
//
//        }
//        catch (MessagingException e)
//        {
//            throw new RuntimeException (e);
//        }
    }

    @RequestMapping("/addpersonhttp")
    public String insertContact(ModelMap model,
                                @ModelAttribute("insertPerson") @Valid Person person,
                                BindingResult result) {
        person.setDate(randomToken());

        if (!personService.throwsErrors(person)) {
            if (!personService.isEmailFree(person.getEmail()))
                model.addAttribute("error_data", "EMAIL IS NOT FREE");
            if (!personService.isLoginFree(person.getLogin()))
                model.addAttribute("error_data1", "LOGIN IS NOT FREE");
            if (!personService.isEmailCorrect(person.getEmail()))
                model.addAttribute("error_data2", "EMAIL IS NOT VALID");
            if (!personService.isPhoneFree(person.getPhoneNumber()))
                model.addAttribute("error_data3", "PHONE NUMBER IS NOT FREE");
            return "error";
        }
        if (!result.hasErrors()) {
            // person.setWhat(2);
            personRepository.save(person);
        }
        return persons(model);
    }

    @RequestMapping("/events")
    public String events(ModelMap model) {
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        model.addAttribute("insertEvent", new Event());
        return "events";
    }

    @RequestMapping("/addeventhttp")
    @ResponseBody
    public String insertEvent(ModelMap model,
                                @ModelAttribute("insertEvent") @Valid Event event,
                                BindingResult result) {
        //event.setTime("DATE");

        if (!result.hasErrors()) {
            eventRepository.save(event);
        }
        return eventAdd(model);
    }

    @RequestMapping("/deleteperson")
    public String deleteContact(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return persons(model);
    }

    @RequestMapping("/deleteevent")
    public String deleteEvent(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        eventRepository.delete(Long.parseLong(id));
        return events(model);
    }

    @RequestMapping("/addmsg")
    @ResponseBody
    public String insertMsg(ModelMap model,
                               @ModelAttribute("msg") String message,
                               @ModelAttribute("time") String time,
                               @ModelAttribute("event_id") int eventId,
                               @ModelAttribute("user_id") int userId,
                               BindingResult result) {
        Message msg = new Message(userId, eventId, time, message);
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

        Person p = new Person(login, pass, lastName, type, email, firstName, rate, phoneNumber,  about, city, date, imageUrl);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            personRepository.save(p);
        }
        return p.toString();
    }


    @RequestMapping("/getpersons")
    @ResponseBody
    public String getPersons(){
        ArrayList<String> arrayList = new ArrayList<>();
         List<Person> persons = personRepository.findAll();

        StringBuilder stringBuilder = new StringBuilder("{ \"persons\": [");

        for (Person p:persons){
             arrayList.add(p.toString());
        }

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }



    @RequestMapping("/getmsgs")
    @ResponseBody
    public String getMsgs(){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Message> messages = messageRepository.findAll();
        for (Message m:messages){
            arrayList.add(m.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"messages\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }


    @RequestMapping("/addevent")
    @ResponseBody
    public String insertEvent(ModelMap model,
                               @ModelAttribute("name") String name,
                               @ModelAttribute("full_name") String time,
                               @ModelAttribute("category") int category,
                               @ModelAttribute("duration") int guideId,
                               @ModelAttribute("place") String place,
                               @ModelAttribute("phone") String duration,
                               @ModelAttribute("description") String description,
                               @ModelAttribute("language") int rate,
                               @ModelAttribute("users_count") int photo,
                               @ModelAttribute("price") int price,
                               BindingResult result) {

        Event e = new Event(place, category, time, duration, price, description, rate, photo, guideId, name);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            eventRepository.save(e);
        }
        return e.toString();
    }


    @RequestMapping("/getevents")
    @ResponseBody
    public String getEvents(){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventRepository.findAll();
        for (Event e:events){
            arrayList.add(e.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"events\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
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
    public String getReviews(){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Review> persons = reviewRepository.findAll();
        for(Review r:persons){
            arrayList.add(r.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"reviews\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
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
    public String getOrders(){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Buy> buys = buyRepository.findAll();
        for(Buy o: buys){
            arrayList.add(o.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"buys\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
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
    public String getPhotos(){
        ArrayList<String> arrayList = new ArrayList<>();
        List<Photo> buys = photoRepository.findAll();
        for(Photo o: buys){
            arrayList.add(o.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"photos\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}
