package com.heroku.demo.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TokenCookies extends PersistentRememberMeToken {

    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tokenValue = "";
    private String username = "";
    private String series = "";
    private Date date;

    public String getUsername() {
        return username;
    }

    public String getSeries() {
        return series;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @PersistenceConstructor
    public TokenCookies(String username, String series, String tokenValue, Date date) {
        super(username, series, tokenValue, date);
        this.username = username;
        this.series = series;
        this.tokenValue = tokenValue;
        this.date = date;
    }

    @PersistenceConstructor
    public TokenCookies(long id, String username, String series, String tokenValue, Date date) {
        super(username, series, tokenValue, date);
        this.id = id;
        this.username = username;
        this.series = series;
        this.tokenValue = tokenValue;
        this.date = date;
    }

    public TokenCookies(){
        super("","","",new Date());
        Logger logger = LoggerFactory.getLogger(TokenCookies.class);
        logger.info("ВЫЗВАЛИ НЕ ТОГО");
    }

    public String getTokenValue() {
        return tokenValue;
    }


}
