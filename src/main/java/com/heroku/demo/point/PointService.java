package com.heroku.demo.point;

/**
 * Created by Keni0k on 25.07.2017.
 */

import java.util.List;

public interface PointService {

    Point addPoint(Point point);
    void delete(long id);
    List<Point> getByGuide(String guide);
    Point editPoint(Point point);
    List<Point> getAll();

}