package com.heroku.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Messages {

    public Messages() {
    }

    public Messages(int guideId, int touristId, String msg, String date) {

        this.guideId = guideId;

        this.touristId = touristId;

        this.msg = msg;

        this.date = date;
    }


    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    private int guideId;

    public int getTouristId() {
        return touristId;
    }

    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    private int touristId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date = "";

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":" + id + ",\n" +
                "\t\"guideId\":" + guideId + ",\n" +
                "\t\"touristId\":" + touristId + ",\n" +
                "\t\"msg\":" + msg + ",\n" +
                "\t\"date\":" + date + ",\n" +
                "}";
    }
}