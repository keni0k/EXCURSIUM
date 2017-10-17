package com.heroku.demo.person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keni0k on 25.07.2017.
 */

public class PersonServiceImpl implements PersonService{

    private PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository){
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
    public List<Person> getByType(int type) {
        return null;
    }

    @Override
    public Person editPerson(Person person) {
        return null;
    }

    @Override
    public Boolean isLoginFree(String login) {
        List<Person> list = new ArrayList<Person>();
        boolean isLog = true;
        for (Person p:list)
            if (p.getLogin().equals(login))
                isLog = false;
        return isLog;
    }

    @Override
    public Boolean authorization(String login, String pass) {
        return null;
    }

    @Override
    public List<Person> getAll() {
        return null;
    }

//    public static List<Person> getByType(RecordRepository recordRepository, int type) {
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