package com.example.pi_back.Services;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditManipulationService {
    @Autowired
    CreditRepository Crepo;

    public Credit retrieveCredit(Long idCredit) {
        Credit credit= Crepo.findById(idCredit) .orElse(null) ;
        return credit ;
    }

    public Amortissement[] TabAmortissementAmortConst(Credit cr) {

        double interest = cr.getInterestRate() / 12;
        double amortissement = cr.getAmount() / (cr.getCreditPeriod() * 12);
        int length = (int) (cr.getCreditPeriod() * 12);

        Amortissement[] ListAmortissement = new Amortissement[length];
        double montantR = cr.getAmount();
        double mensualite = amortissement + montantR * interest;

        for (int i = 0; i < length; i++) {
            Amortissement amort = new Amortissement();
            amort.setMontantR(montantR);
            amort.setMensualite(mensualite);
            amort.setInterest(montantR * interest);
            amort.setAmortissement(amortissement);
            ListAmortissement[i] = amort;

            montantR -= amortissement;
            mensualite = amortissement + montantR * interest;
        }

        return ListAmortissement;
    }
}

