package com.nbc.trello.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // id 자동증가 값 초기화
    @Transactional
    @Modifying
    @Query(
            value = "ALTER TABLE user AUTO_INCREMENT=1 " +
                    "SET @count = 1 " +
                    "UPDATE user SET id = @count\\:=@count+1",
            nativeQuery = true
    )
    void resetId();
}