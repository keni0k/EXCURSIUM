package com.heroku.demo.utils;

import com.heroku.demo.person.PersonController;
import com.heroku.demo.token.TokenCookies;
import com.heroku.demo.token.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenService implements PersistentTokenRepository {

    private TokenRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    public TokenService(TokenRepository tokenRepository){
        repository = tokenRepository;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        logger.info("CREATE_TOKEN: username= " + token.getUsername() + " series= " + token.getSeries() + " tokenValue= " + token.getTokenValue());

        repository.save(new TokenCookies(token.getUsername(), token.getSeries(),
                token.getTokenValue(), token.getDate()));
    }

    @Override
    public void updateToken(String series, String value, Date lastUsed) {
        TokenCookies token = repository.findBySeries(series);
        repository.save(new TokenCookies(token.getId(), token.getUsername(), series, value, lastUsed));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        TokenCookies token = repository.findBySeries(seriesId);
        if (token!=null)
            logger.info("GET_TOKEN: series= "+seriesId + " value= " + token.getTokenValue() + " username= " + token.getUsername());
        return token;
    }

    @Override
    public void removeUserTokens(String username) {
        TokenCookies token = repository.findByUsername(username);
        if (token != null) {
            logger.info("REMOVE_TOKEN: username= " + token.getUsername() + " series= " + token.getSeries() + " tokenValue= " + token.getTokenValue());
            repository.delete(token);
        }
    }
}
