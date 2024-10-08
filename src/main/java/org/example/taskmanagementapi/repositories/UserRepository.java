package org.example.taskmanagementapi.repositories;

import org.example.taskmanagementapi.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(long id);


    boolean existsUserById(Long id);
    boolean existsUserByEmail(String email);
}
