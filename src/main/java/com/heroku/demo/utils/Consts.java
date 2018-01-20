package com.heroku.demo.utils;

public class Consts {
    public static final String URL_PATH = "https://excursium.blob.core.windows.net/img/";
    public static final int EXCURSION_MODERATION = -3;
    public static final int EXCURSION_MODER_FAULT = -2;
    public static final int EXCURSION_BLOCKED = -1;
    public static final int EXCURSION_DISABLED = 0;
    public static final int EXCURSION_ACTIVE = 1;

    public static final int PERSON_DISABLED = -3;
    public static final int PERSON_MODER_GUIDE = -2;
    public static final int PERSON_MODER_TOURIST = -1;
    public static final int PERSON_BLOCKED = 0;
    public static final int PERSON_TOURIST = 1;
    public static final int PERSON_GUIDE = 2;
    public static final int PERSON_ADMIN = 3;

    public static final int LANGUAGE_RU = 0;
    public static final int LANGUAGE_EN = 1;

    public static final int ORDER_DISPUTE = -3;
    public static final int ORDER_DISPUTE_CLOSE = -2;
    public static final int ORDER_CLOSE = -1;
    public static final int ORDER_ADD = 0;
    public static final int ORDER_OPEN = 1;
    public static final int ORDER_CONFIRM = 2;
}
