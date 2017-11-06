package com.heroku.demo;

import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.utils.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(personService));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/index", "/resources/**",
                        "/events/list", "/events/event", "/events/categories", "/events/listjson", "/events/addevent",
                        "/users/user", "/users/getbytoken", "/users/getbyemail", "/users/listjson").permitAll()
                .antMatchers("/users/registration").anonymous()
                .antMatchers("/events/add").hasRole("USER")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/events/list", false)
                .loginPage("/users/login").failureUrl("/users/login?error=true").permitAll()
                .and()
                .logout().logoutUrl("/users/logout").logoutSuccessUrl("/users/login").permitAll()
                .and()
                .rememberMe().key("_spring_security_remember_me").tokenValiditySeconds(1209600);
        http.csrf().disable();
        http.exceptionHandling().accessDeniedPage("/403");
    }

}