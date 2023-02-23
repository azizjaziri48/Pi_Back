package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
