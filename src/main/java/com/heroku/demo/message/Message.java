package com.heroku.demo.message;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {

    public Message(int userId, int eventId, String date, String time) {
        this.userId = userId;
        this.eventId = eventId;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"userId\":\"" + userId +
                "\" , \"eventId\":\"" + eventId + "\" , \"date\":\"" +
                date + "\", \"time\":\"" + time + "\"}";
    }

    public Message() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int eventId = -1;
    private String date = "";
    private int userId = -1;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg = "";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time = "";


    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }


    public long getId() {
        return id;
    }

    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


}