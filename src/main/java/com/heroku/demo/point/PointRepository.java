package com.heroku.demo.point;
import com.heroku.demo.record.Record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("select p from point p where p.id_of_guide = :id_of_guide")
    List<Point> findByGuide(@Param("id_of_guide") int id_of_guide);

}