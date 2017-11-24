package com.heroku.demo.event;

import com.heroku.demo.utils.Consts;
import com.heroku.demo.utils.UtilsForWeb;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "event")
public class Event {

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"place\":\"" + place + "\",\n" +
                "\t\"category\":\"" + category + "\",\n" +
                "\t\"price\":\"" + price + "\",\n" +
                "\t\"description\":\"" + description + "\",\n" +
                "\t\"name\":\"" + name + "\",\n" +
                "\t\"photo_url\":\"" + pathToPhoto + "\",\n" +
                "\t\"language\":\"" + language + "\",\n" +
                "\t\"duration\":\"" + duration + "\",\n" +
                "\t\"users_count\":\"" + usersCount + "\",\n" +
                "\t\"type\":\"" + type + "\"\n" +
                "}";
    }

    public Event() {
    }

    public Event(String place, int category, String time, int duration, int price, String description, int rate, String name, int type, int usersCount, int language) {
        this.place = place;
        this.category = category;
        this.time = time;
        this.duration = duration;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.name = name;
        this.language = language;
        this.usersCount = usersCount;
        this.type = type;
    }

    private int type = -3;

    public long getId() {
        return id;
    }

    @Size(min=5, max=100)
    private String place;

    private int category=0;

    private int reviewsCount = 0;

    private String time;

    @Range(min = 1, max = 240)
    private int duration;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    private int language=0;

    @Range(min = 1, max = 100)
    private int usersCount;

    @Range(min = 1, max = 100000)
    private int price;

    @Size(min=150, max=1000)
    private String description = "";

    private float rate = -1;

    @Transient
    int cnt = 0;

    private String photoOfGuide = "";

    private String fullNameOfGuide = "";

    @Transient
    public String pathToPhoto = "";

    private String city = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long guideId = -1;

    @Size(min=10, max=60)
    private String name = "";

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getLanguageString(){
        return language==0?"RU":"EN";
    }

    public String getSmallDescription() {
        String smallDesc = description.substring(0, description.length() > 251 ? 251 : description.length());
        smallDesc = smallDesc.replace("\\\"", "\"");
        if (smallDesc.length() != description.length()) smallDesc += "...";
        return smallDesc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

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

    public int getCategory() {
        return category;
    }

    public String getCategoryString() {
        return UtilsForWeb.getCategoryString(category, language);
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public float getRate() {
        return rate;
    }

    public int getIntRate() {
        return Math.round(rate);
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getPhotoOfGuide() {
        return Consts.URL_PATH + photoOfGuide;
    }

    public void setPhotoOfGuide(String photoOfGuide) {
        this.photoOfGuide = photoOfGuide;
    }

    public String getFullNameOfGuide() {
        return fullNameOfGuide;
    }

    public void setFullNameOfGuide(String fullNameOfGuide) {
        this.fullNameOfGuide = fullNameOfGuide;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}