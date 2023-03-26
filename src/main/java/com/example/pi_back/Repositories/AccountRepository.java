package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("select a from Account a where a.user.id = ?1")
    Account findByUser_Id(int id);
}
