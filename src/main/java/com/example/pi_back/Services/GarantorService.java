package com.example.pi_back.Services;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.Garantor;
import com.example.pi_back.Repositories.CreditRepository;
import com.example.pi_back.Repositories.GarantorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GarantorService implements IGarantorService {

    @Autowired
    GarantorRepository GRrepo;
    @Autowired
    CreditRepository Crepo;


    @Override
    public List<Garantor> retrieveAllGarantors() {
        return (List<Garantor>) GRrepo.findAll();
    }

    @Override
    public Garantor addGarantor(Garantor garantor, Long idcredit) {
        Credit credit= Crepo.findById(idcredit).orElse(null);
        garantor.setCredit(credit);
        GRrepo.save(garantor);
        return garantor;

    }

    @Override
    public Garantor updateGarantor(Garantor garantor) {
        return GRrepo.save(garantor);
    }

    @Override
    public Garantor retrieveGarantor(Long idGarantor) {
        Garantor garantor= GRrepo.findById(idGarantor) .orElse(null) ;
        return garantor ;
    }


    @Override
    public void DeleteGarantor(Long idGarantor) {
        GRrepo.deleteById(idGarantor);

    }


}
