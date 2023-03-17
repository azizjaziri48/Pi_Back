package com.example.pi_back.Services;


import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Entities.Investment;
import com.example.pi_back.Repositories.FundRepository;
import com.example.pi_back.Repositories.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentService implements IInvestmentService {
	
	@Autowired
	InvestmentRepository InvestmentRepository;
	@Autowired
	FundRepository fundRepository;
	


	float finalA;

	@Override
	public List<Investment> retrieveAllinvestments() {
		return (List<Investment>) InvestmentRepository.findAll();
	}

	@Override
	public Investment addinvestment(Investment i, Long idFund) {
		float Amount = i.getAmountInvestment();

		/*formule de taux economique (invalide)
		i.setTauxInves((i.getAmountInvestment())/(pib*100));
		Le taux d'inves est variable selon le montant choisit
		Plus le montant aug plus le taux aug
		Minimum du montant investit = 7000
		*/
		if (Amount >= 7000) {
			Fund f = fundRepository.findById(idFund).orElse(null);
			//Si le montant atteint 200000 le tau reste fixe à 12%
			double Rate = 0.12*(1- Math.exp(-(Amount)/10000));
			i.setTauxInves((float) Rate);
			i.setFund(f);
			//incrémentation du fund pour chaque investissement
			f.setAmountFund(f.getAmountFund()+i.getAmountInvestment());
			//incrémentation du taux pour chaque investissement
			List<Investment> listInves = (List<Investment>) InvestmentRepository.findAll();
			float s = 0;
			for (Investment  inv : listInves) {
				s=s+(inv.getAmountInvestment());
			}
			s=s+i.getAmountInvestment();
			float pourc_inv = (i.getAmountInvestment())/s;
			f.setTauxFund(pourc_inv*(i.getTauxInves())+(1-pourc_inv)*f.getTauxFund());
			InvestmentRepository.save(i);
		}
		return i;
	}

	@Override
	public Investment updateinvestment(Investment i) {
		float Amount = i.getAmountInvestment();
		double Rate = 0.12*(1- Math.exp(-(Amount)/10000));

		i.setNameInvestment(i.getNameInvestment());
		i.setSecondnameInvestment(i.getSecondnameInvestment());
		i.setAmountInvestment(Amount);
		i.setTauxInves((float) Rate);
		i.setCinInvestment(i.getCinInvestment());
		i.setMailInvestment(i.getMailInvestment());
		i.setProfessionInvestment(i.getProfessionInvestment());
		Investment inv =  InvestmentRepository.save(i);
		return inv;
	}

	@Override
	public Investment retrieveinvestment(Long cininvestment) {
		Investment inves =  InvestmentRepository.findById(cininvestment).orElse(null);
		return inves;
	}

	@Override
	public void CalculateAmountOfInves(Long idInvestissement) {
		Investment inves =  InvestmentRepository.findById(idInvestissement).orElse(null);
		inves.setFinalAmount(inves.getAmountInvestment()+(inves.getAmountInvestment()*inves.getTauxInves()));
			InvestmentRepository.save(inves);
	}
	//test
	//@Scheduled(cron = "10 * * * * *" )
	@Scheduled(cron = "0 0 0 31 12 *" )
	@Override
	public void finalAmount() {
		List<Investment> listInves = (List<Investment>) InvestmentRepository.findAll();
		for (Investment  inv : listInves)
		{	
			inv.setFinalAmount((inv.getAmountInvestment()*(1+inv.getTauxInves())));
			InvestmentRepository.save(inv);
		}
	//	System.out.println("scheduled okay");
	}
//Calcul rate d'investissement  
		float Rate;
		@Override
		public float CalculateRateOfInves(Long idInvestissement) {
			Investment inves =  InvestmentRepository.findById(idInvestissement).orElse(null);
			//Fund f = fundRepository.findById(idFund).orElse(null);
			float Amount = inves.getAmountInvestment();
			double Rate = 0.12*(1- Math.exp(-(Amount)/10000));
			inves.setTauxInves((float) Rate);
			return (float) Rate;
		}		
		
		public double Rate(float AmountInvestestesment) {
			double Rate = 0.12*(1- Math.exp(-(AmountInvestestesment)/10000));
			return  Rate;
		}


		
}
