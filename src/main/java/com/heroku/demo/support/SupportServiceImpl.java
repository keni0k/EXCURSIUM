package com.heroku.demo.support;

import java.util.ArrayList;
import java.util.List;

public class SupportServiceImpl implements SupportService {

    private SupportRepository supportRepository;

    public SupportServiceImpl(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    @Override
    public Support addSupport(Support support) {
        return supportRepository.saveAndFlush(support);
    }

    @Override
    public void delete(long id) {
        supportRepository.delete(id);
    }

    @Override
    public List<Support> getByPerson(long personId) {
        List<Support> list = supportRepository.findAll();
        List<Support> copy = new ArrayList<>();
        for (Support support:list){
            if (support.getPersonId()==personId) {
                copy.add(support);
            }
        }
        return copy;
    }

    @Override
    public Support editSupport(Support support) {
        return supportRepository.saveAndFlush(support);
    }

    @Override
    public List<Support> getAll() {
        return supportRepository.findAll();
    }

    @Override
    public Support getById(long id) {
        return supportRepository.findOne(id);
    }
}
