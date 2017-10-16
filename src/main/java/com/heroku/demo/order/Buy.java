package com.heroku.demo.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Buy {

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int eventId = 0;

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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    private int touristId = 0;

    public Buy(){}

    public Buy(int eventId, int touristId, int price, String orderDate) {
        this.eventId = eventId;
        this.touristId = touristId;
        this.price = price;
        this.orderDate = orderDate;
    }

    private int price = 0;
    private String orderDate = "";

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"event_id\":\"" + eventId + "\",\n" +
                "\t\"tourist_id\":\"" + touristId + "\",\n" +
                "\t\"price\":\"" + price + "\",\n" +
                "\t\"order_date\":\"" + orderDate + "\",\n" +
                "}";
    }

}
