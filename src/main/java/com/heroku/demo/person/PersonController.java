package com.heroku.demo.person;

import com.heroku.demo.event.Event;
import com.heroku.demo.event.EventRepository;
import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.order.Buy;
import com.heroku.demo.order.OrderRepository;
import com.heroku.demo.order.OrderServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.utils.Consts;
import com.heroku.demo.utils.MessageUtil;
import com.heroku.demo.utils.Utils;
import com.heroku.demo.utils.UtilsForWeb;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.Email;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.heroku.demo.utils.Utils.*;

@Controller
@RequestMapping("/users")
public class PersonController {

    private static String AUTH_KEY = "DGgttMjxGUuuLvr49LnEWVFBbkxSNXnH";

    private PersonServiceImpl personService;
    private EventServiceImpl eventService;
    private OrderServiceImpl orderService;

    private final MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    public PersonController(PersonRepository personRepository, MessageSource messageSource, EventRepository eventRepository,
                            ReviewRepository reviewRepository, PhotoRepository photoRepository, OrderRepository orderRepository) {
        orderService = new OrderServiceImpl(orderRepository, eventRepository, photoRepository);
        personService = new PersonServiceImpl(personRepository, eventRepository, reviewRepository, photoRepository);
        eventService = new EventServiceImpl(eventRepository, new PhotoServiceImpl(photoRepository));
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String persons(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "registration";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account(ModelMap model, Principal principal) {
        Person person = new Person();
        if (principal != null) {
            String loginOrEmail = principal.getName();
            if (!loginOrEmail.equals(""))
                person = personService.getByLoginOrEmail(loginOrEmail);
        } else return "login";
        model.addAttribute("person", person);
        model.addAttribute("events", eventService.getByGuideId(person.getId()));
        List<Buy> orders = orderService.getByTourist(person.getId());
        model.addAttribute("orders", orders);
        model.addAttribute("inputEvent", new Event());
        model.addAttribute("consts", new Consts());
        return "account";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String signUp(@ModelAttribute("insertPerson") @Valid Person person,
                         BindingResult result,
                         @RequestParam("file") MultipartFile file,
                         @ModelAttribute("pass2") String pass2,
                         ModelMap model, Locale locale) {
        person.setToken(randomToken(32));

        if (!personService.throwsErrors(person, pass2) || result.hasErrors()) {
            model.addAttribute("error_login", !personService.isLoginFree(person.getLogin()));
            model.addAttribute("error_phone", !personService.isPhoneFree(person.getPhoneNumber()));
            model.addAttribute("error_pass", !person.getPass().equals(pass2));
            model.addAttribute("error_email_free", !personService.isEmailFree(person.getEmail()));
            model.addAttribute("error_email_valid", !personService.isEmailCorrect(person.getEmail()));
            model.addAttribute("insertPerson", person);
            model.addAttribute("message", new MessageUtil("danger", messageSource.getMessage("error.user.add", null, locale)));
            return "registration";
        }
        person.setEmail(person.getEmail().toLowerCase());
        person.setLogin(person.getLogin().toLowerCase());
        if (file == null) {
            model.addAttribute("message", new MessageUtil("danger", "You failed to upload file because the file is null."));// messageSource.getMessage("success.user.registration", null, locale)));
            return persons(model);
        }
        person.setRole("ROLE_USER");
        person.setType(Consts.PERSON_DISABLED);
        String time = new LocalTime().toDateTimeToday().toString().replace('T', ' ');
        time = time.substring(0,time.indexOf('.'));
        person.setTime(time);

        if (!file.isEmpty()) {
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

                String photoToken = randomToken(32) + ".jpg";
                putImg(serverFile.getAbsolutePath(), photoToken);
                person.setImageUrl(photoToken);
                personService.addPerson(person);
                sendMail(person.getToken(), person.getEmail());
                model.addAttribute("message", new MessageUtil("success", messageSource.getMessage("success.user.registration", null, locale)));
            } catch (Exception e) {
                logger.error("You failed to upload file => " + e.getMessage());
                personService.delete(person.getId());
                model.addAttribute("message", new MessageUtil("danger", "You failed to upload file. Please, try again."));// messageSource.getMessage("success.user.registration", null, locale)));
                return persons(model);
            }
            return persons(model);
        } else {
            model.addAttribute("message", new MessageUtil("danger", "You failed to upload file because the file is empty."));// messageSource.getMessage("success.user.registration", null, locale)));
            return persons(model);
        }
    }

    @RequestMapping(value = "/edit_public", method = RequestMethod.POST)
    public String editInfo(@ModelAttribute("first_name") String firstName,
                           @ModelAttribute("last_name") String lastName,
                           @ModelAttribute("about_me") String aboutMe,
                           @ModelAttribute("city") String city,
                           @RequestParam("file") MultipartFile file,
                           ModelMap model, Locale locale, Principal principal) {
        Person person;
        String loginOrEmail = principal.getName();
        if (!loginOrEmail.equals("")) {
            person = personService.getByLoginOrEmail(loginOrEmail);
        } else person = new Person();

        if (file!=null && !file.isEmpty()) {
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

                String photoToken = randomToken(32) + ".jpg";
                putImg(serverFile.getAbsolutePath(), photoToken);
                person.setImageUrl(photoToken);
            } catch (Exception e) {
                logger.error("You failed to upload file => " + e.getMessage());
                model.addAttribute("message_file", new MessageUtil("danger", "You failed to upload file. Please, try again."));// messageSource.getMessage("success.user.registration", null, locale)));
                return account(model, principal);
            }
        }
        person = personService.editPublic(firstName, lastName, city, aboutMe, person, file!=null && !file.isEmpty());
        if (person.getType()== Consts.PERSON_TOURIST || person.getType()==Consts.PERSON_GUIDE)
            person.setType(person.getType()*-1);
        personService.editPerson(person);
        model.addAttribute("message", new MessageUtil("success", messageSource.getMessage("success.user.registration", null, locale)));
        return account(model, principal);
    }

    @RequestMapping(value = "/edit_private", method = RequestMethod.POST)
    public String editPrivate(@ModelAttribute("email") String email,
                              @ModelAttribute("last_pass") String passLast,
                              @ModelAttribute("pass") String pass,
                              @ModelAttribute("pass_confirm") String passConfirm,
                              ModelMap model, Principal principal) {
        Person person;
        String loginOrEmail = principal.getName();
        if (!loginOrEmail.equals("")) {
            person = personService.getByLoginOrEmail(loginOrEmail);
        } else person = new Person();

        if (person.getPass().equals(passLast)) {
            if (pass.equals(passConfirm))
                person.setPass(pass);
        }

        //TODO отработать ошибки и невыполнения if'ов и сделать смену email

        personService.editPerson(person);
        return account(model, principal);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String signIn(ModelMap modelMap) {
        return "login";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirm(ModelMap model, @ModelAttribute("token") String token) {
        Person person = personService.getByToken(token);
        if (person != null)
            if (person.getType() == Consts.PERSON_DISABLED) {
                person.setType(Consts.PERSON_MODER_TOURIST);
                personService.editPerson(person);
                model.addAttribute("message", new MessageUtil("success", "Your account has been successfully verified."));
            }
        return signIn(model);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/getbytoken")
    @ResponseBody
    public ResponseEntity<String> getPerson(@ModelAttribute("token") String token, @ModelAttribute("auth") String authKey) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        Person p = null;
        if (authKey.equals(AUTH_KEY))
            p = personService.getByToken(token);
        return new ResponseEntity<>(p == null ? "{}" : p.toString(), h, HttpStatus.OK);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/getbyemail")
    @ResponseBody
    public ResponseEntity<String> getPersonByEmail(@ModelAttribute("email") String email, @ModelAttribute("auth") String authKey) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        Person p = null;
        if (authKey.equals(AUTH_KEY))
            p = personService.getByEmail(email);
        return new ResponseEntity<>(p == null ? "{}" : p.toString(), h, HttpStatus.OK);
    }

    @RequestMapping("/updatedb")
    @ResponseBody
    public String updateDBPersons() {
        List<Person> persons = personService.getAll();
        for (Person person : persons) {
//                person.setRole("ROLE_USER");
//                personService.editPerson(person);
        }
        return "YES";
    }

    @RequestMapping("/moderation")
    public String persons_last(ModelMap model,
                               @RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "type", required = false) Integer type,
                               @RequestParam(value = "rate_down", required = false) Long rateDown,
                               @RequestParam(value = "rate_up", required = false) Long rateUp,
                               @RequestParam(value = "first_name", required = false) String firstName,
                               @RequestParam(value = "last_name", required = false) String lastName,
                               @RequestParam(value = "city", required = false) String city) {
        List<Person> persons = personService.getByFilter(type, rateDown, rateUp, firstName, lastName, city, null);
        model.addAttribute("persons", persons);
        if (id != null) {
            Person editPerson = personService.getById(id);
            model.addAttribute("insertPerson", editPerson);
        } else model.addAttribute("insertPerson", new Person());
        model.addAttribute("utils", new UtilsForWeb());
        return "persons";
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.POST)
    public String signUpModer(@ModelAttribute("type") int typePerson,
                              @ModelAttribute("city") String cityPerson,
                              @ModelAttribute("id") long idPerson,
                              @ModelAttribute("about") String about,
                              ModelMap model, Locale locale,
                              @RequestParam(value = "type", required = false) Integer type,
                              @RequestParam(value = "rate_down", required = false) Long rateDown,
                              @RequestParam(value = "rate_up", required = false) Long rateUp,
                              @RequestParam(value = "first_name", required = false) String firstName,
                              @RequestParam(value = "last_name", required = false) String lastName,
                              @RequestParam(value = "city", required = false) String city) {
        Person personWithBD = personService.getById(idPerson);
        if (personWithBD == null) logger.info("ERROR PERSONWITHBD IS NULL");
        else {
            personService.editPublic(firstName, lastName, city, about, personWithBD, false);
            if (personWithBD.getType()==Consts.PERSON_BLOCKED && type!=Consts.PERSON_BLOCKED)
                eventsModer(personWithBD.getId());
            if (typePerson==Consts.PERSON_BLOCKED)
                eventsBlock(personWithBD.getId());
            personWithBD.setType(typePerson);
            personService.editPerson(personWithBD);
        }
        model.addAttribute("message", new MessageUtil("success", messageSource.getMessage("success.user.registration", null, locale)));
        return persons_last(model, null, type, rateDown, rateUp, firstName, lastName, city);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteContact(ModelMap model,
                                @ModelAttribute("id") String id,
                                @RequestParam(value = "type", required = false) Integer type,
                                @RequestParam(value = "rate_down", required = false) Long rateDown,
                                @RequestParam(value = "rate_up", required = false) Long rateUp,
                                @RequestParam(value = "first_name", required = false) String firstName,
                                @RequestParam(value = "last_name", required = false) String lastName,
                                @RequestParam(value = "city", required = false) String city) {
        personService.delete(Long.parseLong(id));
        return persons_last(model, null, type, rateDown, rateUp, firstName, lastName, city);
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getPersons(@ModelAttribute("auth") String authKey) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/json;charset=UTF-8");
        ArrayList<String> arrayList = new ArrayList<>();

        List<Person> persons = personService.getAll();

        for (Person p : persons) {
            arrayList.add(p.toString());
        }

        return new ResponseEntity<>(Utils.list("persons", arrayList, authKey, AUTH_KEY), h, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String profile(ModelMap model, @RequestParam("id") long id) {
        Person p = personService.getById(id);
        model.addAttribute("person", p != null ? p : new Person());
        //model.addAttribute("insertEvent", new Event());
        return "profile";
    }

    private String sendMail(String token, String emailStr) throws MailjetSocketTimeoutException, MailjetException {
        MailjetRequest email;
        JSONArray recipients;
        MailjetResponse response;
        MailjetClient client = new MailjetClient("489ff3e95ebe1a6a3303dbd79ec3777f", "0be4f9f8ede6f035f85fd4393875f32d");

        recipients = new JSONArray()
                .put(new JSONObject().put(Contact.EMAIL, emailStr));

        email = new MailjetRequest(Email.resource)
                .property(Email.FROMNAME, "Excursium")
                .property(Email.FROMEMAIL, "elishanto@gmail.com")
                .property(Email.SUBJECT, "Подтвердите свой e-mail")
                .property(Email.TEXTPART, "Вы зарегистрировались на сайте excursium.me и для завершения регистрации должны нажать на ссылку: http://excursium.me/users/confirm?token=" + token)
                .property(Email.RECIPIENTS, recipients)
                .property(Email.MJCUSTOMID, "JAVA-Email");

        response = client.post(email);
        return response.getData() + " " + response.getStatus();
    }

    @RequestMapping(value = "/up_to_guide", method = RequestMethod.POST)
    private String upToGuide(@ModelAttribute("series_and_number") String seriesAndNumber,
                             @ModelAttribute("date_and_place") String dateAndPlace,
                             @RequestParam("file") MultipartFile file,
                             ModelMap model, Locale locale, Principal principal){
        Person person;
        String loginOrEmail = principal.getName();
        if (!loginOrEmail.equals("")) {
            person = personService.getByLoginOrEmail(loginOrEmail);
        } else person = new Person();

        if (file!=null && !file.isEmpty()) {
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

                String photoToken = randomToken(32) + ".jpg";
                putImg(serverFile.getAbsolutePath(), photoToken);
                person.setImageOfPassportUrl(photoToken);
                person.setType(Consts.PERSON_MODER_GUIDE);
                person.setDateAndPlaceOfPassport(dateAndPlace);
                person.setSeriesAndNumberOfPassport(seriesAndNumber);
                personService.editPerson(person);
            } catch (Exception e) {
                logger.error("You failed to upload file => " + e.getMessage());
                model.addAttribute("message_file", new MessageUtil("danger", "You failed to upload file. Please, try again."));// messageSource.getMessage("success.user.registration", null, locale)));
            }
        }
        return account(model, principal);
    }

    @RequestMapping(value = "/resend_email", method = RequestMethod.GET)
    private String reSend(ModelMap model, Locale locale, Principal principal) throws MailjetSocketTimeoutException, MailjetException {
        Person person;
        String loginOrEmail = principal.getName();
        if (!loginOrEmail.equals("")) {
            person = personService.getByLoginOrEmail(loginOrEmail);
        } else person = new Person();
        if (person.getType()==Consts.PERSON_DISABLED)
            sendMail(person.getToken(), person.getEmail());
        return account(model, principal);
    }

    private void eventsBlock(long personId){
        List<Event> events = eventService.getByGuideId(personId);
        for (Event event:events){
            event.setType(Consts.EXCURSION_BLOCKED);
            eventService.editEvent(event);
        }
    }

    private void eventsModer(long personId){
        List<Event> events = eventService.getByGuideId(personId);
        for (Event event:events){
            event.setType(Consts.EXCURSION_MODERATION);
            eventService.editEvent(event);
        }
    }

}
