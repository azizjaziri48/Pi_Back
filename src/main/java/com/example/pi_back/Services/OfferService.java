package com.example.pi_back.Services;

import com.example.pi_back.Entities.Activity;
import com.example.pi_back.Entities.Offer;

import java.util.List;

public interface OfferService {
    List<Offer> retrieveAllOffers();
    Offer AddOffer (Offer offer);
    void removeOffer (int idOffer);
    Offer retrieveOffer (int idOffer);
    Offer updateOffer(Offer offer);
    Offer assignPartnerToOffer(Integer idOffer, Integer idPartner);
}
