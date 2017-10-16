
package com.heroku.demo;

import com.heroku.demo.event.Event;
import com.heroku.demo.event.EventRepository;
import com.heroku.demo.message.Message;
import com.heroku.demo.message.MessageRepository;
import com.heroku.demo.order.Buy;
import com.heroku.demo.order.BuyRepository;
import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import com.heroku.demo.photo.Photo;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.review.Review;
import com.heroku.demo.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {


    private PersonRepository personRepository;
    private MessageRepository messageRepository;
    private EventRepository eventRepository;
    private ReviewRepository reviewRepository;
    private BuyRepository buyRepository;
    private PhotoRepository photoRepository;

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
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "index";
    }

    @RequestMapping("/news")
    public String news(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 0);//findByType(0); //ByType(0);
       // model.addAttribute("records", records);
        //model.addAttribute("insertRecord", new Person());
        return "news";
    }

    @RequestMapping("/addnews")
    public String insertData(ModelMap model,
                             @ModelAttribute("insertRecord") @Valid Person person,
                             BindingResult result) {
        if (!result.hasErrors()) {
            //person.setWhat(0);
            personRepository.save(person);
        }
        return news(model);
    }

    @RequestMapping("/deletenews")
    public String deleteData(ModelMap model, @ModelAttribute("id") String id,
                             BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return news(model);
    }

    @RequestMapping("/guides")
    public String guides(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 1);//findByType(1);
       // model.addAttribute("records", records);
       // model.addAttribute("insertRecord", new Person());
        return "guides";
    }

    @RequestMapping("/addguide")
    public String insertGuide(ModelMap model,
                              @ModelAttribute("insertRecord") @Valid Person person,
                              BindingResult result) {

        if (!result.hasErrors()) {
            //person.setWhat(1);
            personRepository.save(person);
        }
        return guides(model);
    }

    @RequestMapping("/deleteguide")
    public String deleteGuide(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        //TODO: delete points
        return guides(model);
    }

    @RequestMapping("/points")
    public String points(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 1);//findByType(1);
        List<Message> messages = messageRepository.findAll();
       // model.addAttribute("records", records);
        model.addAttribute("messages", messages);
        model.addAttribute("insertPoint", new Message());
        return "messages";
    }

    @RequestMapping("/addpoint")
    public String insertPoint(ModelMap model,
                              @ModelAttribute("insertPoint") @Valid Message message,
                              BindingResult result) {
        if (!result.hasErrors()) {
            messageRepository.save(message);
        }
        return points(model);
    }

    @RequestMapping("/deletepoint")
    public String deletePoint(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        messageRepository.delete(Long.parseLong(id));
        return points(model);
    }

    @RequestMapping("/contacts")
    public String contacts(ModelMap model) {
        //List<Person> records = PersonServiceImpl.getByType(recordRepository, 2);//findByType(1);
        //model.addAttribute("records", records);
        //model.addAttribute("insertRecord", new Person());
        return "contacts";
    }

    @RequestMapping("/addcontact")
    public String insertContact(ModelMap model,
                              @ModelAttribute("insertRecord") @Valid Person person,
                              BindingResult result) {

        if (!result.hasErrors()) {
           // person.setWhat(2);
            personRepository.save(person);
        }
        return contacts(model);
    }


    @RequestMapping("/deletecontact")
    public String deleteContact(ModelMap model, @ModelAttribute("id") String id,
                              BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return contacts(model);
    }


    @RequestMapping("/gallery")
    public String gallery(ModelMap model) {
       // List<Person> records = PersonServiceImpl.getByType(recordRepository, 3);//findByType(1);
       // model.addAttribute("records", records);
        //model.addAttribute("insertRecord", new Person());
        return "gallery";
    }

    @RequestMapping("/addgallery")
    public String insertGallery(ModelMap model,
                                @ModelAttribute("insertRecord") @Valid Person person,
                                BindingResult result) {

        if (!result.hasErrors()) {
            //person.setWhat(3);
            personRepository.save(person);
        }
        return gallery(model);
    }

    @RequestMapping("/deletegallery")
    public String deleteGallery(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        //TODO: delete photos
        return gallery(model);
    }

    @RequestMapping("/photos")
    public String photos(ModelMap model) {
      //  List<Person> records = PersonServiceImpl.getByType(recordRepository, 3);//findByType(1);
        //List<Person> photos = PersonServiceImpl.getByType(recordRepository, 4);
      //  model.addAttribute("records", records);
      //  model.addAttribute("photos", photos);
        //model.addAttribute("insertPhoto", new Person());
        return "photos";
    }

    @RequestMapping("/addphoto")
    public String insertPhotos(ModelMap model,
                                @ModelAttribute("insertPhoto") @Valid Person person,
                                BindingResult result) {

        if (!result.hasErrors()) {
            //person.setWhat(4);
            personRepository.save(person);
        }
        return photos(model);
    }

    @RequestMapping("/deletephoto")
    public String deletePhotos(ModelMap model, @ModelAttribute("id") String id,
                                BindingResult result) {
        personRepository.delete(Long.parseLong(id));
        return photos(model);
    }

    @RequestMapping("/getjson")
    @ResponseBody
    public String getPoints(ModelMap model, @ModelAttribute("type") int type,
            @ModelAttribute("locate") String locate, BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
        switch (type){
            case 5:
                List<Message> messages = messageRepository.findAll();
                for (Message p: messages){
                    arrayList.add(p.toString());
                }
                break;
            default:
                //List<Person> records = PersonServiceImpl.getByTypeAndLocate(recordRepository, type, locate);
              //  for (Person r:records){
              //      arrayList.add(r.toString());
              //  }
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"models\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    @RequestMapping("/getmarshrut")
    @ResponseBody
    public String getMarshrut(ModelMap model, @ModelAttribute("type") String type,
                            @ModelAttribute("locate") String locate, BindingResult result){
        ArrayList<String> arrayList = new ArrayList<>();
       // List<Person> records = PersonServiceImpl.getMarshrutByLocate(recordRepository, type, locate);
       // for (Person r:records){
       //     arrayList.add(r.toString());
       // }

        StringBuilder stringBuilder = new StringBuilder("{ \"models\": [");

        for (int i = 0; i<arrayList.size(); i++) {
            stringBuilder.append(arrayList.get(i));
            if (arrayList.size()-i>1) stringBuilder.append(",\n");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
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
                                BindingResult result) {

        Person p = new Person(login, pass, lastName, type, firstName, email, phoneNumber, rate, about, city);
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
        for (Person p:persons){
             arrayList.add(p.toString());
        }

        StringBuilder stringBuilder = new StringBuilder("{ \"persons\": [");

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
