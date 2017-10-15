package com.heroku.demo.event;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {

    public Event(String name, String date, int id_of_guide, String time) {
        this.name = name;
        this.date = date;
        this.id_of_guide = id_of_guide;
        this.time = time;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"date\":\"" + date +
                "\" , \"id_of_guide\":\"" + id_of_guide + "\" , \"time\":\"" + time + "\"}";
    }

    public Event() {
    }

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long id_of_guide = -1;

    private String name = "";

    private String mesto; //TODO

    private String date = "";

    private String time = "";

    private String prodolzhitelnost = ""; //TODO

    private int price = 0;

    private String opisanie = ""; //TODO

    private int rate = 0;

    private String imgUrl = "";

}