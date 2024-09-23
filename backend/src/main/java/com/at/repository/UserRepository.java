package com.at.repository;

import com.at.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  // Since email is unique, we'll find users by email
  Optional<User> findByEmail(String email);
}
