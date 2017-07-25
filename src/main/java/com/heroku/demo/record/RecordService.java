package com.heroku.demo.record;

import com.heroku.demo.point.Point;

import java.util.List;

/**
 * Created by Keni0k on 25.07.2017.
 */

public interface RecordService {
    Record addRecord(Record record);
    void delete(long id);
    List<Record> getByType(int type);
    Record editRecord(Record record);
    List<Record> getAll();
}
