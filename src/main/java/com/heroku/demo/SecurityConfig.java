package com.heroku.demo;


import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private PersonServiceImpl personService;

    @Autowired
    public SecurityConfig(@Qualifier("dataSource") DataSource dataSource, PersonRepository personRepository) {
        this.dataSource = dataSource;
        personService = new PersonServiceImpl(personRepository);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select login, pass from person where login=?")
                .authoritiesByUsernameQuery("select login, role from person where login=?");
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        List<Person> users = personService.getAll();
        for (Person user:users) {
            String role = user.getRole().substring(user.getRole().indexOf("_")+1);
            auth.inMemoryAuthentication().withUser(user.getLogin()).password(user.getPass()).roles(role);
            auth.inMemoryAuthentication().withUser(user.getEmail()).password(user.getPass()).roles(role);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/resources/**", "/users/registration", "/events/list", "/events/", "/users/").permitAll()
                .antMatchers("/**").hasRole("ADMIN")
                .antMatchers("/events/add").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/", false)
                .loginPage("/users/login").failureUrl("/users/login?error=true").permitAll()
                .and()
                .logout().logoutUrl("/users/login?logout")
                .and()
                .rememberMe().key("_spring_security_remember_me").tokenValiditySeconds(1209600);
        http.csrf().disable();
        http.exceptionHandling().accessDeniedPage("/403");
    }

}