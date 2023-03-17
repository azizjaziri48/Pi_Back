package com.example.pi_back.Services;


import com.example.pi_back.Entities.Investment;

import java.util.List;


public interface IInvestmentService {
	List<Investment> retrieveAllinvestments();

	Investment addinvestment(Investment i , Long idFund);

	Investment updateinvestment(Investment i);

	Investment retrieveinvestment(Long cininvestment);
	
	void CalculateAmountOfInves(Long idinvestment);
	
	float CalculateRateOfInves(Long idInvestissement);

	void finalAmount();
}
