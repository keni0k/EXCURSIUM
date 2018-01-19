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
    public Message addMessage(Message message) {

        return messageRepository.saveAndFlush(message);
    }

    @Override
    public void delete(long id) {
        messageRepository.delete(id);
    }

    public MessageServiceImpl() {
    }

    @Override
    public List<Message> getByEvent(int event) {
        List<Message> list = messageRepository.findAll();
        List<Message> copy = new ArrayList<>(list);
        for (int i = 0; i < copy.size(); i++)
            if (copy.get(i).getEventId() != event) list.remove(i);
        return list;
    }

    public MessageServiceImpl(MessageRepository reviewRepository) {
        this.messageRepository = reviewRepository;
    }

    @Override
    public Message editMessage(Message message) {
        return messageRepository.saveAndFlush(message);
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }
}
