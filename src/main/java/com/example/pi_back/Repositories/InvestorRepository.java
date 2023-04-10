package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Investor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvestorRepository extends CrudRepository<Investor, Integer> {
    Optional<Investor> findById(int investorId);

}
