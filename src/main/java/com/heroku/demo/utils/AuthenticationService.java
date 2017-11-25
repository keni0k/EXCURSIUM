package com.heroku.demo.utils;

import com.heroku.demo.event.EventRepository;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    private final PersonServiceImpl personService;

    @Autowired
    public AuthenticationService(PersonRepository personRepository, EventRepository eventRepository,
                                 ReviewRepository reviewRepository, PhotoRepository photoRepository) {
        this.personService = new PersonServiceImpl(personRepository, eventRepository, reviewRepository, photoRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return personService.getByLoginOrEmail(username);
    }
}