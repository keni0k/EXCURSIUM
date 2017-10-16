package com.heroku.demo.event;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {

//    public Event(String name, String category, int guideId, String time) {
//        this.name = name;
//        this.category = category;
//        this.guideId = guideId;
//        this.time = time;
//    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"name\":\"" + name + "\", \"category\":\"" + category +
                "\" , \"guideId\":\"" + guideId + "\" , \"time\":\"" + time + "\"}";
    }

    public Event() {
    }

    public Event(String place, String category, String time, String duration, int price, String description, int rate, int photo, long guideId, String name) {
        this.place = place;
        this.category = category;
        this.time = time;
        this.duration = duration;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.photo = photo;
        this.guideId = guideId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    private String place = "";

    private String category = "";

    private String time = "";

    private String duration = "";

    private int price = 0;

    private String description = "";

    private int rate = 0;

    private int photo = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long guideId = -1;

    private String name = "";

    public long getGuideId() {
        return guideId;
    }

    public void setGuideId(long guideId) {
        this.guideId = guideId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

}