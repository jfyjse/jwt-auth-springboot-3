package com.jwt.auth.repository;

import com.jwt.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {


//    @Query("""
//select t from Token t inner join User u on t.user.id = u.id
//where t.user.id = :userId and t.loggedOut = false
//""")

    //@Query(value = "SELECT t FROM Token t JOIN t.users u WHERE u.id = ?1 AND t.loggedOut = false", nativeQuery = true)

    @Query(value = "SELECT t.* FROM token t INNER JOIN \"users\" u ON t.user_id = u.id WHERE t.user_id = 1 AND t.is_logged_out = false;", nativeQuery = true)

    List<Token> findAllTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
