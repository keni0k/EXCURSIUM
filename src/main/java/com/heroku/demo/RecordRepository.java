package com.heroku.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("SELECT * FROM record WHERE record.what = :what")
    List<Record> findByType(@Param("what") int what);

}
