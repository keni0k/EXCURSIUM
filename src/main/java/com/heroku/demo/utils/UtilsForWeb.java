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

    public static String getCity(int city, int language) {
        String[] ruRussia = {"Москва", "Санкт-Петербург", "Сочи", "Казань", "Калининград", "Нижний Новгород", "Краснодар", "Ярославль", "Кисловодск", "Вологда"};
        String[] enRussia = {"Moscow", "St. Petersburg", "Sochi", "Kazan", "Kaliningrad", "Nizhny Novgorod", "Krasnodar", "Yaroslavl", "Kislovodsk", "Vologda"};
        if (language == 0) return ruRussia[city];
        else return enRussia[city];
    }

    public int getCitiesCount(){
        return 10;
    }

}
