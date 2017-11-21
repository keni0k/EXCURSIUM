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
    public List<Review> getByEvent(int event) {
        List<Review> list = reviewRepository.findAll();
        List<Review> copy = new ArrayList<>();
        copy.addAll(list);
        for (int i = 0; i < copy.size(); i++)
            if (copy.get(i).getEventId() != event) list.remove(i);
        return list;
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
