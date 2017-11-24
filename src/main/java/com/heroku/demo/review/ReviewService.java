package com.heroku.demo.review;

import java.util.List;

public interface ReviewService {
    Review addReview(Review review);

    void delete(long id);

    List<Review> getByEvent(long type);

    List<Review> getByPerson(long personId);

    Review editReview(Review review);

    List<Review> getAll();
}
