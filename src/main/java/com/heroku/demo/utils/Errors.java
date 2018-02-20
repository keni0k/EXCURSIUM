package com.heroku.demo.utils;

public class Errors {
    private boolean name = false;
    private boolean description = false;
    private boolean place = false;
    private boolean price = false;
    private boolean duration = false;
    private boolean usersCount = false;

    public Errors(boolean name, boolean description, boolean place, boolean price, boolean duration, boolean usersCount) {
        this.name = name;
        this.description = description;
        this.place = place;
        this.price = price;
        this.duration = duration;
        this.usersCount = usersCount;
    }

    public Errors(boolean all) {
        this.name = all;
        this.description = all;
        this.price = all;
        this.place = all;
        this.duration = all;
        this.usersCount = all;
    }

    public boolean isName() {
        return name;
    }

    public boolean isDescription() {
        return description;
    }

    public boolean isPlace() {
        return place;
    }

    public boolean isPrice() {
        return price;
    }

    public boolean isDuration() {
        return duration;
    }

    public boolean isUsersCount() {
        return usersCount;
    }

    public boolean isErrors(){
        return name||description||place||price||duration||usersCount;
    }
}
