package com.heroku.demo.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


//    @Query("SELECT * FROM person WHERE person.what = :what")
//    List<Person> findByType(@Param("what") int what);

}