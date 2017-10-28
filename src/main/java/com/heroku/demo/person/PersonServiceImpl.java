package com.heroku.demo.person;

import java.util.List;

/**
 * Created by Keni0k on 25.07.2017.
 */

public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person addPerson(Person person) {
        personRepository.saveAndFlush(person);
        return person;
    }

    @Override
    public void delete(long id) {
        personRepository.delete(id);
    }

    @Override
    public Person getByToken(String token) {
        List<Person> list = personRepository.findAll();
        for (Person p : list)
            if (p.getToken().equals(token))
                return p;
        return null;
    }

    @Override
    public Person getByEmail(String email) {
        List<Person> list = personRepository.findAll();
        for (Person p : list)
            if (p.getEmail().equals(email))
                return p;
        return null;
    }

    @Override
    public Person getById(long id) {
        return personRepository.findOne(id);
    }

    @Override
    public Person editPerson(Person person) {
        return personRepository.saveAndFlush(person);
    }


    @Override
    public Boolean isLoginFree(String login) {
        List<Person> list = personRepository.findAll();
        boolean isFree = true;
        for (Person p : list)
            if (p.getLogin().equals(login))
                isFree = false;
        return isFree;
    }

    @Override
    public Boolean isEmailFree(String email) {
        List<Person> list = personRepository.findAll();
        boolean isFree = true;
        for (Person p : list)
            if (p.getEmail().equals(email))
                isFree = false;
        return isFree;
    }

    @Override
    public Boolean isEmailCorrect(String target) {
        return target.contains("@") && target.contains(".");
    }

    @Override
    public Boolean throwsErrors(Person person) {
        return isEmailCorrect(person.getEmail()) && isLoginFree(person.getLogin()) && isPhoneFree(person.getPhoneNumber())
                && isEmailFree(person.getEmail());
    }

    @Override
    public Boolean isPhoneFree(String phone) {
        List<Person> list = personRepository.findAll();
        boolean isFree = true;
        for (Person p : list)
            if (p.getPhoneNumber().equals(phone))
                isFree = false;
        return isFree;
    }


    @Override
    public Boolean authorization(String login, String pass) {
        return null;
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

//    public static List<Person> getByToken(RecordRepository recordRepository, int type) {
//        List<Person> list = recordRepository.findAll();
//        List<Person> list1 = new ArrayList<Person>();
//        for (Person r:list)
//            if (r.getWhat() == type) list1.add(r);
//        return list1;
//    }
//
//    public static List<Person> getByTypeAndLocate(RecordRepository recordRepository, int type, String locate) {
//        List<Person> list = recordRepository.findAll();
//        List<Person> list1 = new ArrayList<Person>();
//        for (Person r:list)
//            if ((r.getWhat() == type) && (locate.equals(r.getLocate()))) list1.add(r);
//        return list1;
//    }
//
//    public static List<Person> getMarshrutByLocate(RecordRepository recordRepository, String type, String locate) {
//        List<Person> list = recordRepository.findAll();
//        List<Person> list1 = new ArrayList<Person>();
//        for (Person r:list)
//            if ((r.getWhat() == 1) && (locate.equals(r.getLocate())) && (type.equals(r.getPhone())))  list1.add(r);
//        return list1;
//    }

}