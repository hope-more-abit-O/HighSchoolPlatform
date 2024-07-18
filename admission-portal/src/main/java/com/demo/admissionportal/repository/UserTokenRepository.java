package com.demo.admissionportal.repository;

import com.demo.admissionportal.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface User token repository.
 */
@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

  /**
   * Find all valid token by staff list.
   *
   * @param userId the user id
   * @return the list
   */
  @Query("""
            SELECT t 
            FROM UserToken t INNER JOIN User u ON t.user.id = u.id
            WHERE u.id =  :userId and (t.expired = false or t.revoked = false)
            """)
  List<UserToken> findAllValidTokenByUser(Integer userId);

  /**
   * Find by token staff optional.
   *
   * @param token the token
   * @return the optional
   */
  Optional<UserToken> findByToken(String token);

  /**
   * Find user token by refresh token user token.
   *
   * @param refreshToken the refresh token
   * @return the user token
   */
  UserToken findUserTokenByRefreshToken(String refreshToken);
}