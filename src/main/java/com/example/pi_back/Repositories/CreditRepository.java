package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Integer> {
}