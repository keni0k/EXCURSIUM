package com.heroku.demo.person;

import com.heroku.demo.utils.Consts;
import com.heroku.demo.utils.validation.LoginConstraint;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Person implements UserDetails{

    public long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @LoginConstraint
    private String login = "";
//    @PasswordConstraint
    private String pass = "";
    private String lastName = "";
    private int type = 0;
    private String email = "";
    private String firstName = "";
    private int rate = 0;
    private String phoneNumber = "";
    private String about = "";
    private String city = "";
    private String token = "";
    private String imageUrl = "";
    private String role = "";
    private String time = "";
    private int reviewsCount = 0;

    private String dateAndPlaceOfPassport = "";
    private String seriesAndNumberOfPassport = "";
    private String imageOfPassportUrl = "";


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getType() {
        return type;
    }

    public String getStringType(int language){
        String[] typesRu = {"Не активирован", "Заблокирован", "Турист", "Турист/Экскурсовод", "Администратор"};
        if (language==0) {
            switch (type) {
                case Consts.PERSON_DISABLED: return typesRu[0];
                case Consts.PERSON_BLOCKED: return typesRu[1];
                case Consts.PERSON_TOURIST:
                case Consts.PERSON_MODER_TOURIST:return typesRu[2];
                case Consts.PERSON_GUIDE:
                case Consts.PERSON_MODER_GUIDE:return typesRu[3];
                case Consts.PERSON_ADMIN: return typesRu[4];
                default: return "TYPE NULL";
            }
        } else
            return "LANGUAGE NULL";
    }

    public void setType(int type) {
        this.type = type;
    }

    public Person() {
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImageUrl() {
        return Consts.URL_PATH + imageUrl;
    }

    public String getImageToken(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public float getFloatRate(){
        return (float)rate/reviewsCount;
    }

    public Person(String login, String pass, String lastName, int type, String email, String firstName, int rate, String phoneNumber, String about, String city, String token, String imageUrl) {
        this.login = login;
        this.pass = pass;
        this.lastName = lastName;
        this.type = type;
        this.email = email;
        this.firstName = firstName;
        this.rate = rate;
        this.phoneNumber = phoneNumber;
        this.about = about;
        this.city = city;
        this.token = token;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {

        return "{\n" +

                "\t\"id\":\"" + id + "\",\n" +
                "\t\"login\":\"" + login + "\",\n" +
                "\t\"pass\":\"" + pass + "\",\n" +
                "\t\"lastName\":\"" + lastName + "\",\n" +
                "\t\"firstName\":\"" + firstName + "\",\n" +
                "\t\"type\":\"" + type + "\",\n" +
                "\t\"email\":\"" + email + "\",\n" +
                "\t\"phoneNumber\":\"" + phoneNumber + "\",\n" +
                "\t\"rate\":\"" + rate + "\",\n" +
                "\t\"about\":\"" + about + "\",\n" +
                "\t\"city\":\"" + city + "\",\n" +
                "\t\"token\":\"" + token + "\",\n" +
                "\t\"imageUrl\":\"" + getImageUrl() + "\"\n" +
                "}";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(getRole()));
        return grantedAuths;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return type!=-3;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getDateAndPlaceOfPassport() {
        return dateAndPlaceOfPassport;
    }

    public void setDateAndPlaceOfPassport(String dateAndPlaceOfPassport) {
        this.dateAndPlaceOfPassport = dateAndPlaceOfPassport;
    }

    public String getSeriesAndNumberOfPassport() {
        return seriesAndNumberOfPassport;
    }

    public void setSeriesAndNumberOfPassport(String seriesAndNumberOfPassport) {
        this.seriesAndNumberOfPassport = seriesAndNumberOfPassport;
    }

    public String getImageOfPassportUrl() {
        return imageOfPassportUrl;
    }

    public void setImageOfPassportUrl(String imageOfPassportUrl) {
        this.imageOfPassportUrl = imageOfPassportUrl;
    }
}
