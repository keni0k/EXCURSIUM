package com.heroku.demo.person;

import com.heroku.demo.event.EventController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.heroku.demo.utils.Utils.randomToken;

@Controller
@RequestMapping("/users")
public class PersonController {

    public static String AUTH_KEY = "DGgttMjxGUuuLvr49LnEWVFBbkxSNXnH";

    private PersonServiceImpl personService;

    @Autowired
    public PersonController(PersonRepository personRepository){
        personService = new PersonServiceImpl(personRepository);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String persons(ModelMap model) {
        model.addAttribute("insertPerson", new Person());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String insertContact(ModelMap model,
                                @ModelAttribute("insertPerson") @Valid Person person,
                                BindingResult result) {
        person.setToken(randomToken(32));

        if (!personService.throwsErrors(person)) {
            String errorEmail = "";

            if (!personService.isEmailFree(person.getEmail()))
                errorEmail = errorEmail.concat("EMAIL IS NOT FREE");
            else
            if (!personService.isEmailCorrect(person.getEmail()))
                errorEmail = errorEmail.concat("EMAIL IS NOT VALID");

            model.addAttribute("error_login",!personService.isLoginFree(person.getLogin()));
            if (!personService.isPhoneFree(person.getPhoneNumber())) {
                model.addAttribute("error_phone","PHONE NUMBER IS NOT FREE");
            }
            if (!errorEmail.equals(""))
                model.addAttribute("error_email", errorEmail);
            return persons(model);
        }
        if (!result.hasErrors()) {
            personService.addPerson(person);
        }
        return persons(model);
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

    @RequestMapping("/updatedbpersons")
    @ResponseBody
    public String updateDBPersons(@ModelAttribute("auth") String authKey) {
        if (Objects.equals(authKey, AUTH_KEY)) {
            List<Person> persons = personService.getAll();
            for (Person person : persons) {
                //DOIT
                //personService.editPerson(person);
            }
        }
        return "YES";
    }

    @RequestMapping("/moderation")
    public String persons_last(ModelMap model, @ModelAttribute("auth") String authKey) {
        List<Person> persons = new ArrayList<>();
        if (authKey.equals(AUTH_KEY))
            persons = personService.getAll();
        model.addAttribute("persons", persons);
        model.addAttribute("insertPerson", new Person());
        model.addAttribute("auth", AUTH_KEY);
        model.addAttribute("auth_events", EventController.AUTH_KEY);
        return "persons";
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public String deleteContact(@PathVariable("id") String id, @ModelAttribute("auth") String authKey) {
        if (authKey.equals(AUTH_KEY))
            personService.delete(Long.parseLong(id));
        return persons_last(new ModelMap(), AUTH_KEY);
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String profile(ModelMap model, @PathVariable("id") long id) {
        Person p = personService.getById(id);
        model.addAttribute("person", p != null ? p : new Person());
        //model.addAttribute("insertEvent", new Event());
        return "profile";
    }

}
