package com.heroku.demo.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.heroku.demo.point.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

//    @Query("select p from point p where p.id_of_guide = :id_of_guide")
//    List<Point> findByGuide(@Param("id_of_guide") int id_of_guide);

}
