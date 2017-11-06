package com.heroku.demo.utils;

import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    final PersonServiceImpl personService;

    public CustomAuthenticationProvider(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String name = authentication.getName();
        // You can get the password here
        String password = authentication.getCredentials().toString();

        Person user = personService.getByEmail(name);
        if (user==null) user = personService.getByLogin(name);

        // Your custom authentication logic here
        if (user!=null) {
            if (user.getPass().equals(password))
                return new UsernamePasswordAuthenticationToken(name, password);
            else LoggerFactory.getLogger(CustomAuthenticationProvider.class).info(password);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}