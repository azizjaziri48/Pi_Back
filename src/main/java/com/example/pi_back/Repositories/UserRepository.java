package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}