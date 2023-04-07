package com.example.pi_back.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Entities.Investesment;
import com.example.pi_back.Repositories.FundRepository;
import com.example.pi_back.Repositories.InvestesmentRepository;

@Service
public class InvestesmentService implements IInvestesmentService{

    @Autowired
    InvestesmentRepository investesmentRepository;

    @Autowired
    FundRepository fundRepository;

    //Afficher tous les inves
    @Override
    public List<Investesment> retrieveAllInvestesments() {
        return (List<Investesment>) investesmentRepository.findAll();
    }

    /*l'ajout d'un inves
    le taux sera générer automatiquement
    le fund sera automatiquement mis à jour (montant + taux)
    */
    @Override
    public Investesment addInvestesment(Investesment i, Long idFund) {
        float Amount = i.getAmoutInvestesment();
		/*formule de taux economique (invalide)
		i.setTauxInves((i.getAmoutInvestesment())/(pib*100));
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
            f.setAmountFund(f.getAmountFund()+i.getAmoutInvestesment());
            //incrémentation du taux pour chaque investissement
            List<Investesment> listInves = (List<Investesment>) investesmentRepository.findAll();
            float s = 0;
            for (Investesment  inv : listInves) {
                s=s+(inv.getAmoutInvestesment());
            }
            s=s+i.getAmoutInvestesment();
            float pourc_inv = (i.getAmoutInvestesment())/s;
            f.setTauxFund(pourc_inv*(i.getTauxInves())+(1-pourc_inv)*f.getTauxFund());
            investesmentRepository.save(i);
        }
        return i;
    }

    //mise à jour de l'inves
    @Override
    public Investesment updateInvestesment(Investesment i) {

        float Amount = i.getAmoutInvestesment();
        double Rate = 0.12*(1- Math.exp(-(Amount)/10000));

        i.setNameInvestesment(i.getNameInvestesment());
        i.setSecondnnameInvestesment(i.getSecondnnameInvestesment());
        i.setAmoutInvestesment(Amount);
        i.setTauxInves((float) Rate);
        i.setCinInvestesment(i.getCinInvestesment());
        i.setMailInvestesment(i.getMailInvestesment());
        i.setProfessionInvestesment(i.getProfessionInvestesment());
        Investesment inv =  investesmentRepository.save(i);
        return inv;
    }

    //Afficher un seul inves par son id
    @Override
    public Investesment retrieveInvestesment(Long idInvestesment) {
        Investesment inves =  investesmentRepository.findById(idInvestesment).orElse(null);
        return inves;
    }



    //Afficher une liste d inves par l id du Fund
    public List<Investesment> retrieveInvestesmentbyFund(Long idFund) {
        return (List<Investesment>) investesmentRepository.retrieveInvestesmentbyFund(idFund);
    }


    //Calcul annuel
//	@Scheduled(cron = "0 0 0 31 12 *" )

    //Calcul du Montant recu par l'investisseur apres avoir investit (Montant initial + gain )
    float finalA;
    @Override
    public void CalculateAmoutOfInves(Long idInvestissement) {
        Investesment inves =  investesmentRepository.findById(idInvestissement).orElse(null);
        inves.setFinalAmount(inves.getAmoutInvestesment()+(inves.getAmoutInvestesment()*inves.getTauxInves()));
        investesmentRepository.save(inves);
    }
    //test
    //@Scheduled(cron = "10 * * * * *" )
    @Scheduled(cron = "0 0 0 31 12 *" )
    @Override
    public void finalAmount() {
        List<Investesment> listInves = (List<Investesment>) investesmentRepository.findAll();
        for (Investesment  inv : listInves)
        {
            inv.setFinalAmount((inv.getAmoutInvestesment()*(1+inv.getTauxInves())));
            investesmentRepository.save(inv);
        }
        //	System.out.println("scheduled okay");
    }
    //Calcul rate d'investissement
    float Rate;
    @Override
    public float CalculateRateOfInves(Long idInvestissement) {
        Investesment inves =  investesmentRepository.findById(idInvestissement).orElse(null);
        //Fund f = fundRepository.findById(idFund).orElse(null);
        float Amount = inves.getAmoutInvestesment();
        double Rate = 0.12*(1- Math.exp(-(Amount)/10000));
        inves.setTauxInves((float) Rate);
        return (float) Rate;
    }

    public double Rate(float AmountInvestestesment) {
        double Rate = 0.12*(1- Math.exp(-(AmountInvestestesment)/10000));
        return  Rate;
    }
/*Calcul rate économique (invalide)
float finalrate;
	float pib=(float) 39.24;
	@Override
	public float CalculateRateOfInves(Long idInvestissement,Long idFund) {
		Investesment inves =  investesmentRepository.findById(idInvestissement).orElse(null);
		Fund f = fundRepository.findById(idFund).orElse(null);
		finalrate=(f.getAmountFund())/(pib*100);
		return finalrate;
	}
*/


}
