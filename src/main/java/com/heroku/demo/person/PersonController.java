package com.heroku.demo.person;

import com.heroku.demo.event.Event;
import com.heroku.demo.event.EventRepository;
import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.utils.MessageUtil;
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

import static com.heroku.demo.utils.Utils.*;

@Controller
@RequestMapping("/users")
public class PersonController {

    private static String AUTH_KEY = "DGgttMjxGUuuLvr49LnEWVFBbkxSNXnH";

    private PersonServiceImpl personService;
    private EventServiceImpl eventService;

    private final MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);


    @Autowired
    public PersonController(PersonRepository personRepository, MessageSource messageSource, EventRepository eventRepository,
                            PhotoRepository photoRepository) {
        personService = new PersonServiceImpl(personRepository);
        eventService = new EventServiceImpl(eventRepository,personService, new PhotoServiceImpl(photoRepository));
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String persons(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "registration";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account(ModelMap model, Principal principal, Person person1) {
        Person person;
        if (person1==null) {
            String loginOrEmail = principal.getName();
            if (!loginOrEmail.equals("")) {
                person = personService.getByLoginOrEmail(loginOrEmail);
            } else person = new Person();
        } else
            person = person1;
        model.addAttribute("person", person);
        model.addAttribute("events", eventService.getByGuideId(person.getId()));
        model.addAttribute("inputEvent", new Event());
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
        if (file==null){
            model.addAttribute("message", new MessageUtil("danger", "You failed to upload file because the file is null."));// messageSource.getMessage("success.user.registration", null, locale)));
            return persons(model);
        }
        person.setRole("ROLE_USER");
        person.setType(-3);
        person.setTime(new LocalTime().toDateTimeToday().toString());

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
                person.setImageUrl("https://excursium.blob.core.windows.net/img/"+photoToken);
                personService.addPerson(person);
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

    @RequestMapping(value = "/edit_info", method = RequestMethod.POST)
    public String editInfo(@ModelAttribute("first_name") String firstName,
                           @ModelAttribute("last_name") String lastName,
                           @ModelAttribute("about_me") String aboutMe,
                           @ModelAttribute("city") String city,
                           @ModelAttribute("id") long idPerson,
                           @RequestParam("file") MultipartFile file,
                           ModelMap model, Locale locale) {
        Person person = personService.getById(idPerson);
        if (file==null){
            model.addAttribute("message", new MessageUtil("danger", "You failed to upload file because the file is null."));// messageSource.getMessage("success.user.registration", null, locale)));
            return account(model, null, person);
        }

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
                person.setImageUrl("https://excursium.blob.core.windows.net/img/"+photoToken);
                personService.editPerson(person);
                model.addAttribute("message", new MessageUtil("success", messageSource.getMessage("success.user.registration", null, locale)));
            } catch (Exception e) {
                logger.error("You failed to upload file => " + e.getMessage());
                personService.delete(person.getId());
                model.addAttribute("message", new MessageUtil("danger", "You failed to upload file. Please, try again."));// messageSource.getMessage("success.user.registration", null, locale)));
                return account(model, null, person);
            }
            return account(model, null, person);
        } else {
            model.addAttribute("message", new MessageUtil("danger", "You failed to upload file because the file is empty."));// messageSource.getMessage("success.user.registration", null, locale)));
            return account(model, null, person);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String signIn() {
        return "login";
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getbytoken")
    @ResponseBody
    public String getPerson(@ModelAttribute("token") String token, @ModelAttribute("auth") String authKey) {
        Person p = null;
        if (authKey.equals(AUTH_KEY))
            p = personService.getByToken(token);
        return p == null ? "{}" : p.toString();
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/getbyemail")
    @ResponseBody
    public String getPersonByEmail(@ModelAttribute("email") String email, @ModelAttribute("auth") String authKey) {
        Person p = null;
        if (authKey.equals(AUTH_KEY))
            p = personService.getByEmail(email);
        return p == null ? "{}" : p.toString();
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
        return "persons";
    }

    @RequestMapping(value = "/moderation", method = RequestMethod.POST)
    public String signUpModer(@ModelAttribute("type") int typePerson,
                              @ModelAttribute("city") String cityPerson,
                              @ModelAttribute("id") long idPerson,
                              ModelMap model, Locale locale,
                              @RequestParam(value = "type", required = false) Integer type,
                              @RequestParam(value = "rate_down", required = false) Long rateDown,
                              @RequestParam(value = "rate_up", required = false) Long rateUp,
                              @RequestParam(value = "first_name", required = false) String firstName,
                              @RequestParam(value = "last_name", required = false) String lastName,
                              @RequestParam(value = "city", required = false) String city) {
        Person personWithBD = personService.getById(idPerson);
        if (personWithBD==null) logger.info("ERROR PERSONWITHBD IS NULL");
        else {
            personWithBD.setType(typePerson);
            personWithBD.setCity(cityPerson);
//            personWithBD.setRole(person.getRole().equals("") ? "ROLE_USER" : person.getRole());
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

  /*
    @RequestMapping("/addperson")
    @ResponseBody
    public String insertPerson(@ModelAttribute("pass") String pass,
                               @ModelAttribute("login") String login,
        =                        @ModelAttribute("type") int type,
                               @ModelAttribute("last_name") String lastName,
                               @ModelAttribute("first_name") String firstName,
                               @ModelAttribute("phone_number") String phoneNumber,
                               @ModelAttribute("rate") int rate,
                               @ModelAttribute("city") String city,
                               @ModelAttribute("email") String email,
                               @ModelAttribute("about") String about,
                               @ModelAttribute("date") String date,
                               @ModelAttribute("image_url") String imageUrl,
                               BindingResult result) {

        Person p = new Person(login, pass, lastName, type, email, firstName, rate, phoneNumber, about, city, date, imageUrl);
        if (!result.hasErrors()) {
            //person.setWhat(3);
            personService.addPerson(p);
        }
        return p.toString();
    }*/

    @RequestMapping("/listjson")
    @ResponseBody
    public String getPersons(@ModelAttribute("auth") String authKey) {
        ArrayList<String> arrayList = new ArrayList<>();

        List<Person> persons = new ArrayList<>();
        if (authKey.equals(AUTH_KEY))
            persons = personService.getAll();


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

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String profile(ModelMap model, @RequestParam("id") long id) {
        Person p = personService.getById(id);
        model.addAttribute("person", p != null ? p : new Person());
        //model.addAttribute("insertEvent", new Event());
        return "profile";
    }

}
