package com.heroku.demo.utils;

public class UtilsForWeb {

    public static Consts consts = new Consts();

    public static String getCategoryString(int category, int language) {
        String[] ru = {"Развлечения", "Наука", "История", "Искусство", "Квесты", "Экстрим", "Производство", "Гастрономия"};
        String[] en = {"Entertainment", "Science", "History", "Art", "Quests", "Extreme", "Manufacture", "Gastronomy"};
        if (language == 0) return ru[category];
        else return en[category];
    }

    public static int getCategoriesCount() {
        return 8;
    }

    public static String getCategoryUrl(int category) {
        String[] urls = {"../resources/img/icons/rest.png", "../resources/img/icons/science.png", "../resources/img/icons/history.png",
                "../resources/img/icons/art.png", "../resources/img/icons/quests.png", "../resources/img/icons/extreme.png",
                "../resources/img/icons/production.png", "../resources/img/icons/gastronomy.png"};
        return urls[category];
    }

    public static String getCity(int city, int language) {
        String[] ruRussia = {"Москва", "Санкт-Петербург", "Сочи", "Казань", "Калининград", "Нижний Новгород", "Краснодар", "Ярославль", "Иркутск"};//Kislovodsk, vologda
        String[] enRussia = {"Moscow", "St. Petersburg", "Sochi", "Kazan", "Kaliningrad", "Nizhny Novgorod", "Krasnodar", "Yaroslavl", "Irkutsk"};
        if (language == 0) return ruRussia[city];
        else return enRussia[city];
    }

    public static String getCountryUrl(int country){
        String[] countries = {"../resources/img/icons/russia-ico.png"};
        return countries[country];
    }

    public static String getCityUrl(int country, int city) {
        String[] citiesOfRussia = {"../resources/img/icons/moscow-ico.png", "../resources/img/icons/spb-ico.png", "../resources/img/icons/sochi-ico.png",
                "../resources/img/icons/kazan-ico.png", "../resources/img/icons/kaliningrad-ico.png", "../resources/img/icons/nizhny-novgorod-ico.png",
                "../resources/img/icons/krasnodar-ico.png", "../resources/img/icons/yaroslavl-ico.png", "../resources/img/icons/irkutsk-ico.png"};
        String[] citiesOfItaly = {"Moscow", "St. Petersburg", "Sochi", "Kazan", "Kaliningrad", "Nizhny Novgorod", "Krasnodar", "Yaroslavl", "Irkutsk"};
        switch (country) {
            case 0: return citiesOfRussia[city];
            default: return citiesOfRussia[city];
        }
    }

    public int getCitiesCount(int country){
        switch (country) {
            case 0: return 9;
            default: return 9;
        }
    }

    public int getCountriesCount(){
        return 1;
    }

}
