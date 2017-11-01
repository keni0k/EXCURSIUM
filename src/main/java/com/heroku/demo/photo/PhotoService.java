package com.heroku.demo.photo;

import java.util.List;

public interface PhotoService {
    Photo addPhoto(Photo photo);

    void delete(long id);

    Photo getByEventId(long eventId);

    Photo editPhoto(Photo photo);

    List<Photo> getAll();
}
