package com.heroku.demo.review;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    private ReviewRepository reviewRepository;

    @Override
    public Review addReview(Review review) {

        return reviewRepository.saveAndFlush(review);
    }

    @Override
    public void delete(long id) {
        reviewRepository.delete(id);
    }

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getByEvent(long event) {
        List<Review> list = reviewRepository.findAll();
        List<Review> copy = new ArrayList<>();
        for (Review review:list){
            if (review.getEventId()==event) {
                copy.add(review);
            }
        }
        copy.sort((o1, o2) -> Integer.compare(o2.getLikesCount(), o1.getLikesCount()));
        return copy;
    }

    @Override
    public List<Review> getByPerson(long personId) {
        List<Review> list = reviewRepository.findAll();
        List<Review> copy = new ArrayList<>();
        for (Review review:list){
            if (review.getUserId()==personId) {
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
