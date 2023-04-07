package com.example.pi_back.Services;

import java.util.List;


import com.example.pi_back.Entities.Garantor;

public interface IGarantorService {

    List<Garantor> retrieveAllGarantors();

    Garantor addGarantor (Garantor garantor,Long idcredit);

    Garantor updateGarantor (Garantor garantor);

    Garantor retrieveGarantor(Long idGarantor);

    public void DeleteGarantor(Long idGarantor);


}
