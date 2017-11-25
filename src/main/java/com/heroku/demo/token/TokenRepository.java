package com.heroku.demo.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenCookies, Long> {
    TokenCookies findBySeries(String series);
    TokenCookies findByUsername(String username);
}