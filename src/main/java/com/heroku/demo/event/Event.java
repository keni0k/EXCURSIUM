package com.heroku.demo.event;

import com.heroku.demo.utils.UtilsForWeb;

import javax.persistence.*;

@Entity
public class Event implements Comparable {

//    public Event(String name, String category, int guideId, String time) {
//        this.name = name;
//        this.category = category;
//        this.guideId = guideId;
//        this.time = time;
//    }

    @Override
    public String toString() {

        return "{\n" +
                "\t\"id\":\"" + id + "\",\n" +
                "\t\"place\":\"" + place + "\",\n" +
                "\t\"category\":\"" + category + "\",\n" +
                "\t\"price\":\"" + price + "\",\n" +
                "\t\"description\":\"" + description + "\",\n" +
                "\t\"name\":\"" + name + "\",\n" +
                "\t\"photo_url\":\"" + photoUrl + "\",\n" +
                "\t\"language\":\"" + language + "\",\n" +
                "\t\"duration\":\"" + duration + "\",\n" +
                "\t\"users_count\":\"" + usersCount + "\",\n" +
                "\t\"type\":\"" + type + "\"\n" +
                "}";
    }

    public Event() {
    }

    public Event(String place, int category, String time, int duration, int price, String description, int rate, int photo, String name, int type, int usersCount, int language) {
        this.place = place;
        this.category = category;
        this.time = time;
        this.duration = duration;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.photo = photo;
        this.name = name;
        this.language = language;
        this.usersCount = usersCount;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type = -1;

    public long getId() {
        return id;
    }

    private String place = "";

    private int category = 0;

    private String time = "";

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl = "";

    private int duration = -1;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    private int language = 0;

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public String getLanguageString(){
        return language==0?"RU":"EN";
    }

    private int usersCount = 1;

    private int price = 0;

    private String description = "";

    private int rate = -1;

    @Transient
    int cnt = 0;

    @Transient
    public String photoOfGuide = "";

    @Transient
    public String fullNameOfGuide = "";

    @Transient
    public String pathToPhoto = "";

    private int photo = 0;

    public String getSmallDescription() {
        String smallDesc = description.substring(0, description.length() > 251 ? 251 : description.length());
        smallDesc = smallDesc.replace("\\\"", "\"");
        if (smallDesc.length() != description.length()) smallDesc += "...";
        return smallDesc;
    }

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

    @Override
    public int compareTo(Object o) {
        Event e = (Event) o;
        return Long.compare(getId(), e.getId());
    }
}