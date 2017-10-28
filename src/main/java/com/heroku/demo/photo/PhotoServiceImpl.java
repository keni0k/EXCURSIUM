package com.heroku.demo.photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoServiceImpl implements PhotoService {

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    private PhotoRepository photoRepository;

    @Override
    public Photo addPhoto(Photo phot) {

        return photoRepository.saveAndFlush(phot);
    }

    @Override
    public void delete(long id) {
        photoRepository.delete(id);
    }

    public PhotoServiceImpl(PhotoRepository reviewRepository) {
        this.photoRepository = reviewRepository;
    }

    @Override
    public List<Photo> getByType(int event) {
        List<Photo> list = photoRepository.findAll();
        List<Photo> copy = new ArrayList<>();
        copy.addAll(list);
        for (int i = 0; i < copy.size(); i++)
            if (copy.get(i).getEventId() != event) list.remove(i);
        return list;
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
