package com.heroku.demo.photo;

import java.util.List;

public interface PhotoService {
    Photo addPhoto(Photo photo);

    void delete(long id);

    List<Photo> getByEventId(long eventId);

    Photo getByToken(String token);

    Photo editPhoto(Photo photo);

    List<Photo> getAll();
}
