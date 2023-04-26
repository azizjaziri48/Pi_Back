package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Investesment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvestesmentRepository  extends CrudRepository<Investesment, Long>{

    @Query(value = "SELECT  i  FROM Investesment i WHERE i.fund.idFund= ?1")
    List<Investesment> retrieveInvestesmentbyFund(Long idFund);


}
