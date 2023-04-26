package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Entities.Transaction;
import com.example.pi_back.Entities.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByAccount(Account account);

}



//recherche







