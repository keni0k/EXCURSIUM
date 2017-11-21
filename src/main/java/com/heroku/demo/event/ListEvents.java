package com.heroku.demo.event;

import java.util.ArrayList;

class ListEvents extends ArrayList<Event> {
    int getMaxPrice() {
        return maxPrice;
    }

    void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    int getMinPrice() {
        return minPrice;
    }

    void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    private int maxPrice = 0;
    private int minPrice = 100000;

    String getMinMax(){
        return minPrice+";"+maxPrice;
    }

}
