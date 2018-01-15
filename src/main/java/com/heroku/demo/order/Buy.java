package com.heroku.demo.order;

import javax.persistence.*;

@Entity
public class Buy {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int eventId = 0;

    private int price = 0;

    private String time = "";

    private long touristId = 0;

    private long reviewId = -1;

    @Transient
    private String imageUrl;

    private String name;

    private String description;

    public Buy() {
    }

    public Buy(int eventId, int touristId, int price, String time) {
        this.eventId = eventId;
        this.touristId = touristId;
        this.price = price;
        this.time = time;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public long getTouristId() {
        return touristId;
    }

    public void setTouristId(int touristId) {
        this.touristId = touristId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"event_id\":\"" + eventId + "\",\n" +
                "\t\"tourist_id\":\"" + touristId + "\",\n" +
                "\t\"price\":\"" + price + "\",\n" +
                "\t\"order_date\":\"" + time + "\",\n" +
                "}";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
