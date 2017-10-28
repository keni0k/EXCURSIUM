package com.heroku.demo.utils;

public class UtilsForWeb {
    public static String getCategoryString(int category, int language) {
        String[] ru = {"Развлечения", "Наука", "История", "Искусство", "Производство", "Гастрономия", "Квесты", "Экстрим"};
        String[] en = {"Entertainment", "Science", "History", "Art", "Manufacture", "Gastronomy", "Quests", "Extreme"};
        if (language == 0) return ru[category];
        else return en[category];
    }

    public static int getCategoriesCount() {
        return 8;
    }

    public static String getCategoryUrl(int category) {
        String[] urls = {"../resources/img/icons/rest.png", "../resources/img/icons/science.png", "../resources/img/icons/history.png",
                "../resources/img/icons/art.png", "../resources/img/icons/production.png", "../resources/img/icons/gastronomy.png",
                "../resources/img/icons/quests.png", "../resources/img/icons/extreme.png"};
        return urls[category];
    }
}
