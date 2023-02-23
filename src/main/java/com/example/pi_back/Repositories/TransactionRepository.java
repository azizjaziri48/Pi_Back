package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}