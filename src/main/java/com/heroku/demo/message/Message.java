package com.heroku.demo.message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {

    public Message(int userId, int eventId, String time, String msg) {
        this.userId = userId;
        this.eventId = eventId;
        this.time = time;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"userId\":\"" + userId +
                "\" , \"eventId\":\"" + eventId + "\" , \"date\":\"" +
                time + "\", \"msg\":\"" + msg + "\"}";
    }

    public Message() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int eventId = -1;
    private String time = "";
    private int userId = -1;
    private int type = -1;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg = "";


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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}