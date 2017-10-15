package com.heroku.demo.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

//    @Query("select p from message p where p.id_of_guide = :id_of_guide")
//    List<Message> findByGuide(@Param("id_of_guide") int id_of_guide);

}
