package com.heroku.demo.message;

/**
 * Created by Keni0k on 25.07.2017.
 */

import java.util.ArrayList;
import java.util.List;

public class MessageServiceImpl implements MessageService {

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    private MessageRepository messageRepository;

    @Override
    public Message addPoint(Message message) {

        return messageRepository.saveAndFlush(message);
    }

    @Override
    public void delete(long id) {
        messageRepository.delete(id);
    }

    public MessageServiceImpl() {
    }

    @Override
    public List<Message> getByGuide(int guide) {
        List<Message> list = messageRepository.findAll();
        List<Message> copy = new ArrayList<>();
        copy.addAll(list);
        for (int i = 0; i<copy.size(); i++)
            if (copy.get(i).getId_of_guide()!=guide) list.remove(i);
        return list;
    }

    @Override
    public Message editPoint(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }
}
