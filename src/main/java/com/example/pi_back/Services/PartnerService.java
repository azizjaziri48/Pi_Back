package com.example.pi_back.Services;

import com.example.pi_back.Entities.Partner;

import java.util.List;

public interface PartnerService {
    List<Partner> retrieveAllPartners();
    Partner AddPartner (Partner partner);
    void removePartner (int idPartner);
    Partner retrievePartner (int idPartner);
    Partner updatePartner(Partner partner);
}
