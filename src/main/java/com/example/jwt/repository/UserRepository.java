package com.example.jwt.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.jwt.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}
