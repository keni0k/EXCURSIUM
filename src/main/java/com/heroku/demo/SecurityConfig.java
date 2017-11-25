package com.heroku.demo;

import com.heroku.demo.event.EventRepository;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.utils.AuthenticationService;
import com.heroku.demo.utils.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PersonServiceImpl personService;

    private final AuthenticationService authenticationService;

    private final PersistentTokenBasedRememberMeServices persistenceTokenRepository;

    @Autowired
    public SecurityConfig(PersonRepository personRepository, EventRepository eventRepository,
                          ReviewRepository reviewRepository, PhotoRepository photoRepository, PersistentTokenBasedRememberMeServices persistenceTokenRepository, AuthenticationService authenticationService) {
        personService = new PersonServiceImpl(personRepository, eventRepository, reviewRepository, photoRepository);
        this.persistenceTokenRepository = persistenceTokenRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(personService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/resources/**",
                        "/events/list", "/events/event", "/events/categories", "/events/listjson", "/events/addevent",
                        "/users/user", "/users/getbytoken", "/users/getbyemail", "/users/listjson", "/users/account",
                        "/reviews/listjson").permitAll()
                .antMatchers("/users/registration").anonymous()
                .antMatchers("/events/add", "/users/edit_public", "/users/edit_private", "/reviews/add").hasAnyRole("ADMIN","USER")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/users/account", false)
                .loginPage("/users/login").failureUrl("/users/login?error=true").permitAll()
                .and()
                .logout().logoutUrl("/users/logout").logoutSuccessUrl("/users/login").permitAll()
                .and()
                .rememberMe().tokenValiditySeconds(1209600).rememberMeParameter("remember-me").rememberMeCookieName("remember_me").
                tokenRepository((PersistentTokenRepository) persistenceTokenRepository)
                .and()
                .exceptionHandling().accessDeniedPage("/errors/403")
                .and()
                .csrf().disable();
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices persistenceTokenBasedservice =
                new PersistentTokenBasedRememberMeServices("remember_me", authenticationService, (PersistentTokenRepository) persistenceTokenRepository);
        persistenceTokenBasedservice.setAlwaysRemember(true);
        return persistenceTokenBasedservice;
    }

}