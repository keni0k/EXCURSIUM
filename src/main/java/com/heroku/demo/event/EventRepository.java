package com.heroku.demo.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT c FROM Event AS c WHERE c.price<=:price_up AND c.price>=:price_down " +
            "AND (c.language=:language OR :language=-1) AND (c.category=:category OR :category=-1) " +
            "AND (c.type=0 OR :is_all)")
    List<Event> getByFilter(@Param("price_up") Integer priceUp, @Param("price_down") Integer priceDown,
                            @Param("category") Integer category, @Param("language") Integer language,
                            @Param("is_all") Boolean isAll);
}
