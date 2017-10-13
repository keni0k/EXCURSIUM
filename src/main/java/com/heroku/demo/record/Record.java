package com.heroku.demo.record;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Record {

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
