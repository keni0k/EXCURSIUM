package com.heroku.demo.record;

/**
 * Created by Keni0k on 25.07.2017.
 */

import com.heroku.demo.point.Point;
import com.heroku.demo.point.PointRepository;
import com.heroku.demo.point.PointService;

import java.util.ArrayList;
import java.util.List;

public class RecordServiceImpl {

    public static List<Record> getByType(RecordRepository recordRepository, int type) {
        List<Record> list = recordRepository.findAll();
        List<Record> list1 = new ArrayList<Record>();
        for (Record r:list)
            if (r.getWhat() == type) list1.add(r);
        return list1;
    }

    public static List<Record> getByTypeAndLocate(RecordRepository recordRepository, int type, int locate) {
        List<Record> list = recordRepository.findAll();
        List<Record> list1 = new ArrayList<Record>();
        for (Record r:list)
            if ((r.getWhat() == type) && (locate == r.getLocate())) list1.add(r);
        return list1;
    }
}