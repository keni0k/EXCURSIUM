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
                "\t\"photo_of_guide\":\"" + getPhotoOfGuide() + "\",\n" +
                "\t\"reviews_count\":\"" + reviewsCount + "\",\n" +
                "\t\"full_name_of_guide\":\"" + fullNameOfGuide + "\",\n" +
                "\t\"city\":\"" + city + "\",\n" +
                "\t\"language\":\"" + language + "\",\n" +
                "\t\"duration\":\"" + duration + "\",\n" +
                "\t\"users_count\":\"" + usersCount + "\",\n" +
                "\t\"rate\":\"" + rate + "\",\n" +
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

    @Range(min = 100, max = 100000)
    private int price;

    @Size(min=150, max=1000)
    private String description = "";

    private float rate = -1;

    @Transient
    int cnt = 0;

    private int ageLimit = 0;

    private String photoOfGuide = "";

    private String fullNameOfGuide = "";

    @Transient
    public String pathToPhoto = "";

    private int city = -1;

    private int country = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long guideId = -1;

    @Size(min=10, max=60)
    private String name = "";

    private String activeDates = "0000000";

    private int typeOfDates = 0;

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
        String smallDesc = description.substring(0, description.length() > 180 ? 180 : description.length());
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

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getStringType(int language){
        String[] ru = {"На модерации", "Не прошла модерацию","Заблокирована", "Не активна", "Активна"};//TODO:translate
        if (language==0) {
            switch (type) {
                case Consts.EXCURSION_MODERATION:return ru[0];
                case Consts.EXCURSION_MODER_FAULT: return ru[1];
                case Consts.EXCURSION_BLOCKED: return ru[2];
                case Consts.EXCURSION_DISABLED: return ru[3];
                case Consts.EXCURSION_ACTIVE: return ru[4];
                default: return "TYPE NULL";
            }
        } else
            return "LANGUAGE NULL";
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public void setCountryAndCity(String countryAndCity) {
        String[] mas = countryAndCity.split("\\.");
        country = Integer.parseInt(mas[0]);
        city = Integer.parseInt(mas[1]);
    }

    public String getAgeLimitString(){
        String s = " age18";
        if (ageLimit<=16) s+=" age16";
        if (ageLimit<=12) s+=" age12";
        if (ageLimit<=6) s+=" age6";
        if (ageLimit==0) s+=" age0";
        return s;
    }

    public String getActiveDates() {
        return activeDates;
    }

    public void setActiveDates(String activeDates) {
        this.activeDates = activeDates;
    }

    public int getTypeOfDates() {
        return typeOfDates;
    }

    public void setTypeOfDates(int typeOfDates) {
        this.typeOfDates = typeOfDates;
    }
}