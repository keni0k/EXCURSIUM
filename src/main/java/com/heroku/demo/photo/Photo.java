package com.heroku.demo.photo;

import com.heroku.demo.utils.Consts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Photo {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int eventId = 0;

    public Photo(int eventId, String data) {
        this.eventId = eventId;
        this.data = data;
    }

    public Photo() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getData() {
        return Consts.URL_PATH + data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data = "";

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"event_id\":\"" + eventId + "\",\n" +
                "\t\"data\":\"" + data + "\",\n" +
                "}";
    }
}
