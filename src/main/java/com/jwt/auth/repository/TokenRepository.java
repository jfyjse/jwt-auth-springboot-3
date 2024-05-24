package com.jwt.auth.repository;

import com.jwt.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = "SELECT t.* FROM token t INNER JOIN \"users\" u ON t.user_id = u.id WHERE t.user_id = 1 AND t.is_logged_out = false;", nativeQuery = true)

    List<Token> findAllTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
