package com.heroku.demo.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login = "";
    private String pass = "";
    private String lastName = "";
    private int type = 0;
    private String email = "";
    private String firstName = "";
    private int rate = 0;
    private String phoneNumber = "";
    private String about = "";
    private String city = "";


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Person(){}

    public Person(String login, String pass
            , String lastName, int type, String firstName
            , String email, String phoneNumber, int rate, String about, String city) {

        this.email = email;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.rate = rate;
        this.about = about;
        this.city = city;
        this.login = login;
        this.pass = pass;
        this.type = type;
        this.lastName = lastName;
    }

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"login\":\"" + login + "\",\n" +
                "\t\"pass\":\"" + pass + "\",\n" +
                "\t\"lastName\":\"" + lastName + "\",\n" +
                "\t\"firstName\":\"" + firstName + "\",\n" +
                "\t\"type\":\"" + type + "\",\n" +
                "\t\"email\":\"" + email + "\",\n" +
                "\t\"phoneNumber\":\"" + phoneNumber + "\",\n" +
                "\t\"rate\":\"" + rate + "\",\n" +
                "\t\"about\":\"" + about + "\",\n" +
                "\t\"city\":\"" + city + "\"\n" +
                "}";
    }

}
