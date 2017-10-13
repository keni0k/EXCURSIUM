package com.heroku.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class Users {

    public Users() {
    }

    public Users(String login, String password, String name){
//            , String surname, int type, String email, String phoneNumber, int rate, String about, String city) {

        this.login = login;

        this.password = password;

        this.name = name;
//
//        this.surname = surname;
//
//        this.type = type;
//
//        this.email = email;
//
//        this.phoneNumber = phoneNumber;
//
//        this.rate = rate;
//
//        this.about = about;
//
//        this.city = city;
    }


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

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    private String name = "";
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
//
//    private String surname = "";
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    private int type = 0;
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    private String city = "";
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    private String email = "";
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    private String phoneNumber = "";
//
//    public int getRate() {
//        return rate;
//    }
//
//    public void setRate(int rate) {
//        this.rate = rate;
//    }
//
//    private int rate = 0;
//
//    public String getAbout() {
//        return about;
//    }
//
//    public void setAbout(String about) {
//        this.about = about;
//    }
//
//    private String about = "";


    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":" + id + ",\n" +
                "\t\"login\":" + login + ",\n" +
                "\t\"password\":" + password + ",\n" +
//                "\t\"name\":" + name + ",\n" +
//                "\t\"surname\":" + surname + ",\n" +
//                "\t\"type\":" + type + ",\n" +
//                "\t\"email\":" + email + ",\n" +
//                "\t\"phoneNumber\":" + phoneNumber + ",\n" +
//                "\t\"rate\":" + rate + ",\n" +
//                "\t\"about\":" + about + "\n" +
                "}";
    }
}