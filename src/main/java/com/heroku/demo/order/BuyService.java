package com.heroku.demo.order;

import java.util.List;

public interface BuyService {
    Buy addBuy(Buy buy);
    void delete(long id);
    List<Buy> getByEvent(int guide);
    Buy editBuy(Buy buy);
    List<Buy> getAll();
}
