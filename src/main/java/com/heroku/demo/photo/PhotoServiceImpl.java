package com.heroku.demo.photo;

import java.util.List;

public class PhotoServiceImpl implements PhotoService {

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    private PhotoRepository photoRepository;

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
    public Photo getByEventId(long eventId) {
        List<Photo> list = photoRepository.findAll();
        for (Photo photo: list) {
            if (photo.getEventId()==eventId) return photo;
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
