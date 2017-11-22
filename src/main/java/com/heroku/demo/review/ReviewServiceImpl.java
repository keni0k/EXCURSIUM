package com.heroku.demo.review;

import com.heroku.demo.person.Person;
import com.heroku.demo.person.PersonServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    private ReviewRepository reviewRepository;

    private PersonServiceImpl personService;

    @Override
    public Review addReview(Review review) {

        return reviewRepository.saveAndFlush(review);
    }

    @Override
    public void delete(long id) {
        reviewRepository.delete(id);
    }

    public ReviewServiceImpl(ReviewRepository reviewRepository, PersonServiceImpl personService) {
        this.reviewRepository = reviewRepository;
        this.personService = personService;
    }

    @Override
    public List<Review> getByEvent(int event) {
        List<Review> list = reviewRepository.findAll();
        List<Review> copy = new ArrayList<>();
        for (Review review:list){
            if (review.getEventId()==event) {
                Person p = personService.getById(review.getUserId());
                if (p!=null){
                    review.pathToUserPhoto = p.getImageUrl();
                    review.userFullName = p.getFullName();
                }
                copy.add(review);
            }
        }
        return copy;
    }

    @Override
    public Review editReview(Review review) {
        return reviewRepository.saveAndFlush(review);
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }
}
