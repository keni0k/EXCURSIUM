package com.heroku.demo.support;

import java.util.List;

public interface SupportService {

    Support addSupport(Support support);

    void delete(long id);

    List<Support> getByPerson(long personId);

    Support editSupport(Support support);

    List<Support> getAll();

    Support getById(long id);
}
