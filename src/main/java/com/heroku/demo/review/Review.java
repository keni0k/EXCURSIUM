package com.heroku.demo.review;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Review {

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

    public Review(){}

    public Review(String login, String pass
            , String lastName, int type){
//            , String surname, int type, String email, String phoneNumber, int rate, String about, String city) {

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
                "\t\"last_name\":\"" + lastName + "\",\n" +
//                "\t\"surname\":" + surname + ",\n" +
                "\t\"type\":\"" + type + "\",\n" +
//                "\t\"email\":" + email + ",\n" +
//                "\t\"phoneNumber\":" + phoneNumber + ",\n" +
//                "\t\"rate\":" + rate + ",\n" +
//                "\t\"about\":" + about + "\n" +
                "}";
    }

}
