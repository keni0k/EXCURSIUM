package com.heroku.demo.order;

import com.heroku.demo.event.EventRepository;
import com.heroku.demo.event.EventServiceImpl;
import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonRepository;
import com.heroku.demo.person.PersonServiceImpl;
import com.heroku.demo.photo.PhotoRepository;
import com.heroku.demo.photo.PhotoServiceImpl;
import com.heroku.demo.review.ReviewRepository;
import com.heroku.demo.utils.Utils;
import com.heroku.demo.utils.UtilsForWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private OrderServiceImpl orderService;
    private PersonServiceImpl personService;
    private EventServiceImpl eventService;
    private String AUTH_KEY = "Rtdk6BKb1nDK6Opl38dlYPhIBzmCISY8";

    @Autowired
    public OrderController(OrderRepository orderRepository, EventRepository eventRepository, PhotoRepository photoRepository, PersonRepository personRepository, ReviewRepository reviewRepository){
        orderService = new OrderServiceImpl(orderRepository, new EventServiceImpl(eventRepository, new PhotoServiceImpl(photoRepository)));
        personService = new PersonServiceImpl(personRepository, eventRepository, reviewRepository, photoRepository);
        eventService = new EventServiceImpl(eventRepository, new PhotoServiceImpl(photoRepository));
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String event(ModelMap model, @RequestParam("id") long id, Principal principal) {
        model.addAttribute("utils", new UtilsForWeb());
        if (principal != null) {
            String loginOrEmail = principal.getName();
            if (!loginOrEmail.equals("")) {
                Person person = personService.getByLoginOrEmail(loginOrEmail);
                if (orderService.findByOrder(person.getId(), id)) {
                    model.addAttribute("person", person);
                    Buy order = orderService.getById(id);
                    model.addAttribute("order", order);
                    model.addAttribute("event", eventService.getById(order.getEventId()));
                    return "order/order";
                }
            }
        }
        return "error/403";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String orderAdd(ModelMap model, @Valid Buy order, BindingResult result) {
        if (!result.hasErrors())
            orderService.addOrder(order);
        return "person/account";
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    public String listJson(@RequestParam("auth") String authKey) {
        List<Buy> orderList = orderService.getAll();
        return Utils.list("orders", orderList, authKey, AUTH_KEY);
    }


}
