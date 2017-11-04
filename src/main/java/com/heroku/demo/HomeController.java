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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Iterator;
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

    private String eventAddAgain(ModelMap model,
                                Event event, String errorData) {
        model.addAttribute("inputEvent", event);
        model.addAttribute("error_data", errorData);
        return "event_add";
    }

    @RequestMapping("/event")
    public String event(ModelMap model, @ModelAttribute("id") int id) {
        model.addAttribute("event", eventService.getById(id));
        return "event";
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
                                                    @RequestParam(value = "price_down", required = false) Integer priceDown,
                                                    @RequestParam(value = "price_up", required = false) Integer priceUp,
                                                    @RequestParam(value = "category", required = false) Integer category,
                                                    @RequestParam(value = "words", required = false) String words,
                                                    @RequestParam(value = "language", required = false) Integer language) {
        if (language==null) language = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words, false);
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
        MailjetClient client = new MailjetClient("489ff3e95ebe1a6a3303dbd79ec3777f", "0be4f9f8ede6f035f85fd4393875f32d");

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

    @RequestMapping(value = "/events")
    public String events(ModelMap model,
                            @RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "price_up", required = false) Integer priceUp,
                            @RequestParam(value = "price_down", required = false) Integer priceDown,
                            @RequestParam(value = "category", required = false) Integer category,
                            @RequestParam(value = "language", required = false) Integer language,
                            @RequestParam(value = "words", required = false) String words,
                            @RequestParam(value = "sort_by", required = false) Integer sortBy) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words, sortBy);
        if (id != null) {
            Event editEvent = eventService.getById(id);
            model.addAttribute("insertEvent", editEvent);
        } else model.addAttribute("insertEvent", new Event());
        model.addAttribute("events", events);
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

    @RequestMapping(value = "/event_list")
    public String eventsByFilter(ModelMap model,
                                 @RequestParam(value = "category", required = false) Integer category,
                                 @RequestParam(value = "language", required = false) Integer language,
                                 @RequestParam(value = "price_up", required = false) Integer priceUp,
                                 @RequestParam(value = "price_down", required = false) Integer priceDown,
                                 @RequestParam(value = "words", required = false) String words) {
        List<Event> events = eventService.getByFilter(priceUp, priceDown, category, language, words, true);
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

        String photoToken = randomToken(32) + ".jpg";
        photoRepository.save(new Photo((int) eventId, photoToken));//todo
        CloudStorageAccount account = CloudStorageAccount.parse("DefaultEndpointsProtocol=https;AccountName=excursium;AccountKey=fbMSD2cjYX08BJeKQvNM4Wk87I7fGWJShZvdtR3BdwvhXKUFuYv//qtJs9eAKmESG4Ib7CAHDJlgOIxSw5wwfg==;EndpointSuffix=core.windows.net");
        CloudBlobClient client = account.createCloudBlobClient();
        CloudBlobContainer container = client.getContainerReference("img");
        CloudBlockBlob blob1 = container.getBlockBlobReference(photoToken);
        blob1.uploadFromFile(path);
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "jpg";
    }

    private File compress(File file, String extension, double fileSize) throws IOException {

        BufferedImage image = ImageIO.read(file);

        File compressedImageFile = new File("compress.jpg");
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(extension);
        ImageWriter writer = writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        float quality = (float) (1f / fileSize);
        quality = quality>1?1:quality;
        param.setCompressionQuality(quality);
        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();

        File outputfile = new File("image.jpg");
        ImageIO.write(image, "jpg", outputfile);

        return outputfile;
    }

    private static double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }

    @RequestMapping(value = "/addeventhttp", method = RequestMethod.POST)
    public String insertEvent(@ModelAttribute("inputEvent") @Valid Event event,
                              BindingResult result,
                              @RequestParam("file") MultipartFile file,
                              ModelMap modelMap) {
        event.setTime(new LocalTime().toDateTimeToday().toString());
        event.setGuideId(-1);
        event.setPhotoUrl("URL");
        if (!result.hasErrors()) {
            eventService.editEvent(event);
        } else {
            modelMap.addAttribute("file", file);
            modelMap.addAttribute("insertEvent", event);
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

                putImg(event.getId(), serverFile.getAbsolutePath());
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


    @RequestMapping("/deleteperson")
    public String deleteContact(@ModelAttribute("id") String id) {
        personService.delete(Long.parseLong(id));
        return persons_last(new ModelMap());
    }

    @RequestMapping("/deleteevent")
    public String deleteEvent(ModelMap model, @ModelAttribute("id") Long id,
                              @RequestParam(value = "price_up", required = false) Integer priceUp,
                              @RequestParam(value = "price_down", required = false) Integer priceDown,
                              @RequestParam(value = "category", required = false) Integer category,
                              @RequestParam(value = "language", required = false) Integer language,
                              @RequestParam(value = "words", required = false) String words) {
        eventService.delete(id);
        return events(model, null, priceUp, priceDown, category, language, words, null);
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
    public String insertEvent(@ModelAttribute("name") String name,
                              @ModelAttribute("category") int category,
                              @ModelAttribute("place") String place,
                              @ModelAttribute("duration") int duration,
                              @ModelAttribute("description") String description,
                              @ModelAttribute("language") int language,
                              @ModelAttribute("users_count") int usersCount,
                              @ModelAttribute("price") int price,
                              @ModelAttribute("photo") String photo,
                              BindingResult result) {
        Event e = new Event(place, category, new LocalTime().toDateTimeToday().toString(), duration, price, description, -1, name, -1, usersCount, language);
        if (!result.hasErrors()) {//todo
            //person.setWhat(3);
            eventService.addEvent(e);
            if (photo != null)
                photoService.addPhoto(new Photo((int) e.getId(), photo));
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

    @RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
    public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files) {

        StringBuilder message = new StringBuilder();
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());

                message.append("You successfully uploaded file=").append(name).append("<br />");
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }
        return message.toString();
    }

}
