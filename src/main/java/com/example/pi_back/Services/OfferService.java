package com.example.pi_back.Services;


import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Partner;


import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface OfferService {
    List<Offer> retrieveAllOffers();
    Offer AddOffer (Offer offer) throws MessagingException, IOException;
    void removeOffer (int idOffer);
    Offer retrieveOffer (int idOffer);
    Offer updateOffer(Offer offer);
    Offer assignPartnerToOffer(Integer idOffer, Integer idPartner);
    List<Offer> findByPartner_Name(String name);
    List<Offer> historiqueOffers(int idUser);
}
