package com.heroku.demo.record;

/**
 * Created by Keni0k on 25.07.2017.
 */

import com.heroku.demo.point.Point;
import com.heroku.demo.point.PointRepository;
import com.heroku.demo.point.PointService;

import java.util.List;

public class RecordServiceImpl implements RecordService {

    public RecordRepository getRecordRepository() {
        return recordRepository;
    }
    private RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public Record addRecord(Record record) {

        return recordRepository.saveAndFlush(record);

    }

    @Override
    public void delete(long id) {
        recordRepository.delete(id);
    }

    @Override
    public List<Record> getByType(int type) {
        List<Record> list = recordRepository.findAll();
        for (Record r:list) {
            if (r.getWhat()!=type)list.remove(r);
        }
        return list;
    }

    @Override
    public Record editRecord(Record record) {
        return recordRepository.saveAndFlush(record);
    }

    @Override
    public List<Record> getAll() {
        return recordRepository.findAll();
    }
}