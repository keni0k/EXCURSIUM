package com.heroku.demo.message;

/**
 * Created by Keni0k on 25.07.2017.
 */

import java.util.List;

public interface MessageService {

    Message addMessage(Message message);

    void delete(long id);

    List<Message> getByEvent(int guide);

    Message editMessage(Message message);

    List<Message> getAll();

}