package com.heroku.demo.review;

import com.heroku.demo.photo.Photo;

import java.util.List;

public interface ReviewService {
    Review addReview(Review review);
    void delete(long id);
    List<Review> getByType(int type);
    Review editReview(Review review);
    List<Review> getAll();
}
