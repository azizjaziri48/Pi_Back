package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}