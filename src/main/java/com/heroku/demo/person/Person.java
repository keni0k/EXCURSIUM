package com.heroku.demo.person;

import com.heroku.demo.utils.Consts;
import com.heroku.demo.utils.validation.LoginConstraint;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Person {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @LoginConstraint
    private String login = "";
//    @PasswordConstraint
    private String pass = "";
    private String lastName = "";
    private int type = 0;
    private String email = "";
    private String firstName = "";
    private int rate = 0;
    @Size(min = 17, max = 17)
    private String phoneNumber = "";
    private String about = "";
    private String city = "";
    private String token = "";
    private String imageUrl = "";
    private String role = "";
    private String time = "";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public Person() {
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImageUrl() {
        return Consts.URL_PATH + imageUrl;
    }

    public String getImageToken(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Person(String login, String pass, String lastName, int type, String email, String firstName, int rate, String phoneNumber, String about, String city, String token, String imageUrl) {
        this.login = login;
        this.pass = pass;
        this.lastName = lastName;
        this.type = type;
        this.email = email;
        this.firstName = firstName;
        this.rate = rate;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.city = city;
        this.token = token;
        this.imageUrl = imageUrl;
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
                "\t\"city\":\"" + city + "\",\n" +
                "\t\"token\":\"" + token + "\",\n" +
                "\t\"imageUrl\":\"" + getImageUrl() + "\"\n" +
                "}";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
