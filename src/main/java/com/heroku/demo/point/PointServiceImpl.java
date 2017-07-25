package com.heroku.demo.point;

/**
 * Created by Keni0k on 25.07.2017.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

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

    @Override
    public List<Point> getByGuide(int guide) {
        return pointRepository.findByGuide(guide);
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
