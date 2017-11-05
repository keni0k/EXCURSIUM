package com.heroku.demo;


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

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select login, pass from person where login=?")
                .authoritiesByUsernameQuery("select login, role from person where login=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/", "/index", "/resources/**", "/users/registration", "/events/list", "/users/updatedb").permitAll()
                .antMatchers("/users/moderation","/events/moderation").hasRole("ADMIN")
                .antMatchers("/logout").hasRole("USER")

                .anyRequest().authenticated().and().formLogin().defaultSuccessUrl("/", false)
                .loginProcessingUrl("/security_check").loginPage("/users/login").passwordParameter("pass").usernameParameter("login")
                .failureUrl("/users/login?error=true").permitAll()

                .and().logout();
        http.exceptionHandling().accessDeniedPage("/403");
    }

}