package com.example.pi_back.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Entities.Investesment;


@Repository
public interface InvestesmentRepository  extends CrudRepository<Investesment, Long>{

    @Query(value = "SELECT  i  FROM Investesment i WHERE i.fund.idFund= ?1")
    List<Investesment> retrieveInvestesmentbyFund(Long idFund);


}
