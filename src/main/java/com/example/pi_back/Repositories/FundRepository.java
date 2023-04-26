package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Fund;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FundRepository  extends CrudRepository<Fund, Long>{

}
