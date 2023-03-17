package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Investment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvestmentRepository extends CrudRepository<Investment, Long>{

	@Query(value = "SELECT  i  FROM Investment i WHERE i.fund.idFund= ?1")
	List<Investment> retrieveInvestmentbyFund(Long idFund);


	

}
