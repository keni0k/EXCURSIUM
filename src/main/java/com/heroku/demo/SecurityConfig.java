package com.heroku.demo;

import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.utils.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PersonServiceImpl personService;

    @Autowired
    public SecurityConfig(PersonRepository personRepository) {
        personService = new PersonServiceImpl(personRepository);
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
                .rememberMe().tokenValiditySeconds(1209600)
                .and()
                .exceptionHandling().accessDeniedPage("/errors/403")
                .and()
                .csrf().disable();
    }

}