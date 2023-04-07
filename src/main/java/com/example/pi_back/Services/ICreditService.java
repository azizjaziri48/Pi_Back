package com.example.pi_back.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.Garantor;

@Service
public interface ICreditService  {
    List<Credit> retrieveAllCredits();

    Credit addCredit (Credit credit,Integer Id_client,Long Id_fund,Long Id_garant);

    Credit updateCredit (Credit credit,Integer Id_client,Long Id_fund);

    Credit retrieveCredit(Long idCredit);

    Credit ArchiveCredit(Long idCredit);

    Amortissement Simulateur(Credit credit);

    Credit retrieveActiveCredit(Integer clientid);

    Credit retrievelastCredit(Integer clientid);

    public void DeleteCredit(Long id);

    float Calcul_mensualite(Credit cr);

}
