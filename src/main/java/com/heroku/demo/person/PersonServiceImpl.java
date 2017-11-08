package com.heroku.demo.person;

import java.util.ArrayList;
import java.util.Comparator;
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

    public Person getByLoginOrEmail(String loginOrEmail) {
        List<Person> list = personRepository.findAll();
        for (Person p : list)
            if (p.getLogin().equals(loginOrEmail))
                return p;
            else if (p.getEmail().equals(loginOrEmail))
                return p;
        return null;
    }

    @Override
    public List<Person> getByFilter(Integer type, Long rateDown, Long rateUp, String firstName, String lastName, String city, Integer sortBy) {

        if (rateDown==null) rateDown = -1L;
        if (rateUp==null) rateUp = 5L;
        if (type==null) type = -1;
        if (firstName==null) firstName = "";
        if (lastName==null) lastName = "";
        List<Person> list = getAll();

        List<Person> copy = new ArrayList<>();
        for (Person aList : list) {
            boolean rate = (aList.getRate() >= rateDown) && (aList.getRate() <= rateUp);
            boolean isEqualsType = aList.getType() == type || type == -1;
            boolean isContainsFirstName = aList.getFirstName().toLowerCase().contains(firstName.toLowerCase());
            boolean isContainsLastName = aList.getLastName().toLowerCase().contains(lastName.toLowerCase());
            boolean isContainsCity = aList.getCity().toLowerCase().contains(city.toLowerCase());
            if (rate && isEqualsType && isContainsFirstName && isContainsLastName && isContainsCity)
                copy.add(aList);
        }
        copy.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                switch (sortBy) {
                    case 0:
                        return Long.compare(o1.getId(), o2.getId());
                    case 1:
                        return Long.compare(o2.getId(), o1.getId());
                    case 2:
                        return Integer.compare(o1.getRate(), o2.getRate());
                    case 3:
                        return Integer.compare(o2.getRate(), o1.getRate());
                    case 4:
                        return Integer.compare(o1.getType(), o2.getType());
                    case 5:
                        return Integer.compare(o2.getType(), o1.getType());
                    case 6:
                        return o1.getFirstName().compareTo(o2.getFirstName());
                    case 7:
                        return o2.getFirstName().compareTo(o1.getFirstName());
                    case 8:
                        return o1.getLastName().compareTo(o2.getLastName());
                    case 9:
                        return o2.getLastName().compareTo(o1.getLastName());
                    case 10:
                        return o1.getCity().compareTo(o2.getCity());
                    case 11:
                        return o2.getCity().compareTo(o1.getCity());

                }
                return Long.compare(o1.getId(), o2.getId());
            }
        });
        return copy;
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
    public Boolean throwsErrors(Person person, String pass2) {
        return isEmailCorrect(person.getEmail()) && isLoginFree(person.getLogin()) && isPhoneFree(person.getPhoneNumber())
                && isEmailFree(person.getEmail()) && person.getPass().equals(pass2);
    }

    @Override
    public Boolean isPhoneFree(String phone) {
        List<Person> list = getAll();
        boolean isFree = true;
        for (Person p : list)
            if (p.getPhoneNumber().equals(phone))
                isFree = false;
        return isFree;
    }


    @Override
    public Boolean authorization(String login, String pass) {
        login = login.toLowerCase();
        List<Person> list = personRepository.findAll();
        for (Person p : list)
            if (p.getLogin().equals(login) || p.getEmail().equals(login))
                if (p.getPass().equals(pass))
                    return true;
        return false;
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