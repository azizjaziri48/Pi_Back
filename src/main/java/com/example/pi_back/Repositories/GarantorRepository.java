package com.example.pi_back.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.pi_back.Entities.Garantor;

@Repository

public interface GarantorRepository extends CrudRepository<Garantor, Long> {

}