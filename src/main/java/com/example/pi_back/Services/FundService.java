package com.example.pi_back.Services;


import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Repositories.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundService implements IFundService {

	@Autowired
	FundRepository fundrepository;
	
	@Override
	public List<Fund> retrieveAllFunds() {
		return (List<Fund>) fundrepository.findAll();
	}

	@Override
	public Fund addFund(Fund f) {
		return fundrepository.save(f);	
	}

	@Override
	public void deleteFund(Long idFund) {
		fundrepository.deleteById(idFund);	
	}

	@Override
	public Fund updateFund(Fund fun) {
		fundrepository.save(fun)	;
		return fun;
	}

	@Override
	public Fund retrieveFund(Long idFund) {
		return fundrepository.findById(idFund).orElse(null);	
	}

}
