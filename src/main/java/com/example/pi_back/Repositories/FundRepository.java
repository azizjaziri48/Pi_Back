package com.example.pi_back.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.pi_back.Entities.Fund;


@Repository
public interface FundRepository  extends CrudRepository<Fund, Long>{

}
