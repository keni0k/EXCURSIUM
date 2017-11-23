package com.heroku.demo.review;

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
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewServiceImpl reviewService;
    private String AUTH_KEY = "6l3fWPOarneLHt2OAOObbLC5p9UuJGPb";

    @Autowired
    public ReviewController(ReviewRepository reviewRepository){
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String reviewAdd(ModelMap model, @Valid Review review, BindingResult result) {
        if (!result.hasErrors())
            reviewService.addReview(review);
        return "account";
    }

    @RequestMapping(value = "/listjson", method = RequestMethod.POST)
    public String listJson(@RequestParam("auth") String authKey) {
        List<Review> reviewList = reviewService.getAll();

        StringBuilder stringBuilder = new StringBuilder("{ \"reviews\": [");
        if (authKey.equals(AUTH_KEY)) {
            for (int i = 0; i < reviewList.size(); i++) {
                stringBuilder.append(reviewList.get(i));
                if (reviewList.size() - i > 1) stringBuilder.append(",\n");
            }
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

}
