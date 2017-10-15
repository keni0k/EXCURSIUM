package com.heroku.demo.message;

/**
 * Created by Keni0k on 25.07.2017.
 */

import java.util.List;

public interface MessageService {

    Message addPoint(Message message);
    void delete(long id);
    List<Message> getByGuide(int guide);
    Message editPoint(Message message);
    List<Message> getAll();

}