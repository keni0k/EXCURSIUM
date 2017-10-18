
package com.heroku.demo;

import com.heroku.demo.event.Event;
import com.heroku.demo.event.EventRepository;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

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
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping("/persons")
    public String persons(ModelMap model) {
        List<Person> persons = personRepository.findAll();
        model.addAttribute("persons", persons);
        model.addAttribute("insertPerson", new Person());
        return "persons";
    }

    @RequestMapping("/events_andrey")
    public String eventAdd(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "event_add";
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
        model.addAttribute("insertEvent", new Person());
        return "events";
    }

    @RequestMapping("/addeventhttp")
    public String insertEvent(ModelMap model,
                                @ModelAttribute("insertEvent") @Valid Event event,
                                BindingResult result) {
        event.setTime("DATE");

        if (!result.hasErrors()) {
            eventRepository.save(event);
        }
        return events(model);
    }

    @RequestMapping("/deleteperson")
    public String deleteContact(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return persons(model);
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
                               @ModelAttribute("category") int category,
                               @ModelAttribute("guide_id") int guideId,
                               @ModelAttribute("place") String place,
                               @ModelAttribute("duration") String duration,
                               @ModelAttribute("description") String description,
                               @ModelAttribute("rate") int rate,
                               @ModelAttribute("photo") int photo,
                               @ModelAttribute("price") int price,
                               BindingResult result) {

        Event e = new Event(place, category, "TIME", duration, price, description, rate, photo, guideId, name);
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
