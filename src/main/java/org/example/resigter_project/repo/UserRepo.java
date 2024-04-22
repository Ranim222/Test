package org.example.resigter_project.repo;

import org.example.resigter_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo  extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
}
