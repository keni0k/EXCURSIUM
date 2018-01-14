package com.heroku.demo.order;

import com.heroku.demo.event.EventRepository;
import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private OrderServiceImpl orderService;
    private String AUTH_KEY = "Rtdk6BKb1nDK6Opl38dlYPhIBzmCISY8";

    @Autowired
    public OrderController(OrderRepository orderRepository, EventRepository eventRepository, PhotoRepository photoRepository){
        orderService = new OrderServiceImpl(orderRepository, new EventServiceImpl(eventRepository, new PhotoServiceImpl(photoRepository)));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String orderAdd(ModelMap model, @Valid Buy order, BindingResult result) {
        if (!result.hasErrors())
            orderService.addOrder(order);
        return "account";
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    public String listJson(@RequestParam("auth") String authKey) {
        List<Buy> orderList = orderService.getAll();
        return Utils.list("orders", orderList, authKey, AUTH_KEY);
    }


}
