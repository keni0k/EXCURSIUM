package com.heroku.demo.person;

import java.util.List;

/**
 * Created by Keni0k on 25.07.2017.
 */

public interface RecordService {
    Person addRecord(Person person);
    void delete(long id);
    List<Person> getByType(int type);
    Person editRecord(Person person);
    List<Person> getAll();
}
