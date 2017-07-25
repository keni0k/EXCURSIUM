package com.heroku.demo.point;

/**
 * Created by Keni0k on 25.07.2017.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PointServiceImpl implements PointService {

    public PointRepository getPointRepository() {
        return pointRepository;
    }

    @Autowired
    private PointRepository pointRepository;

    @Override
    public Point addPoint(Point point) {

        return pointRepository.saveAndFlush(point);
    }

    @Override
    public void delete(long id) {
        pointRepository.delete(id);
    }

    public PointServiceImpl() {
    }

    @Override
    public List<Point> getByGuide(String guide) {
        List<Point> list = pointRepository.findAll();
        List<Point> copy = new ArrayList<>();
        copy.addAll(list);
        for (int i = 0; i<copy.size(); i++)
            if (!copy.get(i).getId_of_guide().equals(guide)) list.remove(i);
        return list;
    }

    @Override
    public Point editPoint(Point point) {
        return pointRepository.saveAndFlush(point);
    }

    @Override
    public List<Point> getAll() {
        return pointRepository.findAll();
    }
}
