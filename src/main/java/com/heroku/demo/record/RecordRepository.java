package com.heroku.demo.record;

import com.heroku.demo.record.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

//    @Query("SELECT * FROM record WHERE record.what = :what")
//    List<Record> findByType(@Param("what") int what);

}
