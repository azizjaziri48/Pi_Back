package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Garantor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GarantorRepository extends CrudRepository<Garantor, Long> {

}