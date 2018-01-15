package com.heroku.demo.review;

import com.heroku.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewServiceImpl reviewService;
    private String AUTH_KEY = "6l3fWPOarneLHt2OAOObbLC5p9UuJGPb";

    @Autowired
    public ReviewController(ReviewRepository reviewRepository){
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    public String listJson(@RequestParam("auth") String authKey) {
        List<Review> reviewList = reviewService.getAll();
        return Utils.list("reviews", reviewList, authKey, AUTH_KEY);
    }

}
