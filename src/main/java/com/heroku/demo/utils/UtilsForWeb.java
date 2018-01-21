package com.heroku.demo.utils;

public class UtilsForWeb {

    public static Consts consts = new Consts();

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

    public static String[] getCitiesStringRussia(int language) {
        String[] ruRussia = {"Москва", "Санкт-Петербург", "Новосибирск", "Волгоград", "Арангельск", "Иркутск", "Красноярск", "Калининград"};
        String[] enRussia = {"Moscow", "St. Petersburg", "Novosibirsk", "Volgograd", "Arkhangelsk", "Irkutsk", "Krasnoyarsk", "Kaliningrad"};
        if (language == 0) return ruRussia;
        else return enRussia;
    }

}
