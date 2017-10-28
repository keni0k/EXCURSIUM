package com.heroku.demo.order;

import java.util.ArrayList;
import java.util.List;

//public interface BuyServiceImpl {
//}

public class BuyServiceImpl implements BuyService {

    public BuyRepository getBuyRepository() {
        return buyRepository;
    }

    private BuyRepository buyRepository;

    @Override
    public Buy addBuy(Buy buy) {

        return buyRepository.saveAndFlush(buy);
    }

    @Override
    public void delete(long id) {
        buyRepository.delete(id);
    }

    public BuyServiceImpl(BuyRepository reviewRepository) {
        this.buyRepository = reviewRepository;
    }

    @Override
    public List<Buy> getByEvent(int event) {
        List<Buy> list = buyRepository.findAll();
        List<Buy> copy = new ArrayList<>();
        copy.addAll(list);
        for (int i = 0; i < copy.size(); i++)
            if (copy.get(i).getEventId() != event) list.remove(i);
        return list;
    }

    @Override
    public Buy editBuy(Buy buy) {
        return buyRepository.saveAndFlush(buy);
    }

    @Override
    public List<Buy> getAll() {
        return buyRepository.findAll();
    }
}
