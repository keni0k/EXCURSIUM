package com.heroku.demo.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {


//    @Query("SELECT * FROM person WHERE person.what = :what")
//    List<Person> findByType(@Param("what") int what);

}
