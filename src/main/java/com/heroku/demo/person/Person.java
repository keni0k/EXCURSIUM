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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String login = "";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password = "";

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String lastName = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type = 0;

    public Person(){}

    public Person(String login, String password
            , String lastName, int type){
//            , String surname, int type, String email, String phoneNumber, int rate, String about, String city) {

        this.login = login;

        this.password = password;

        this.type = type;
        this.lastName = lastName;
    }

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":" + id + ",\n" +
                "\t\"login\":" + login + ",\n" +
                "\t\"password\":" + password + ",\n" +
                "\t\"lastName\":" + lastName + ",\n" +
//                "\t\"surname\":" + surname + ",\n" +
                "\t\"type\":" + type + ",\n" +
//                "\t\"email\":" + email + ",\n" +
//                "\t\"phoneNumber\":" + phoneNumber + ",\n" +
//                "\t\"rate\":" + rate + ",\n" +
//                "\t\"about\":" + about + "\n" +
                "}";
    }

}
