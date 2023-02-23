package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.InternalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternalServiceRepository extends JpaRepository<InternalService, Integer> {
}