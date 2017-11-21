package com.heroku.demo.review;

import java.util.List;

public interface ReviewService {
    Review addReview(Review review);

    void delete(long id);

    List<Review> getByEvent(int type);

    Review editReview(Review review);

    List<Review> getAll();
}
