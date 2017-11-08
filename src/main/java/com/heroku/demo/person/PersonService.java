package com.heroku.demo.person;

import java.util.List;

/**
 * Created by Keni0k on 25.07.2017.
 */

public interface PersonService {
    Person addPerson(Person person);

    void delete(long id);

    Person getByToken(String token);

    Person getByEmail(String email);

    Person getById(long id);

    Person editPerson(Person person);

    Boolean isLoginFree(String login);

    Boolean isEmailFree(String login);

    Boolean isEmailCorrect(String login);

    Boolean throwsErrors(Person person, String pass2);

    Boolean isPhoneFree(String login);

    Boolean authorization(String login, String pass);

    List<Person> getAll();

    List<Person> getByFilter(Integer type, Long rateDown, Long rateUp, String firstName, String lastName, String city, Integer sortBy);
}
