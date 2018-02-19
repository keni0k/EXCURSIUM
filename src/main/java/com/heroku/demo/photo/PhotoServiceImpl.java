package com.heroku.demo.photo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PhotoServiceImpl implements PhotoService {

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    private PhotoRepository photoRepository;

    private static final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);

    @Override
    public Photo addPhoto(Photo photo) {

        return photoRepository.saveAndFlush(photo);
    }

    @Override
    public void delete(long id) {
        photoRepository.delete(id);
    }

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public List<Photo> getByEventId(long eventId) {
        List<Photo> list = getAll();
        ArrayList<Photo> solve = new ArrayList<>();
        for (Photo photo: list) {
            if (photo.getEventId()==eventId)
                solve.add(photo);
        }
        solve.sort(Comparator.comparingInt(Photo::getNumber));
        return solve;
    }

    @Override
    public Photo getByToken(String token) {
        List<Photo> list = getAll();
        for (Photo photo: list) {
            if (photo.getToken().equals(token)) {
                return photo;
            }
        }
        return null;
    }

    @Override
    public Photo editPhoto(Photo photo) {
        return photoRepository.saveAndFlush(photo);
    }

    @Override
    public List<Photo> getAll() {
        return photoRepository.findAll();
    }
}
