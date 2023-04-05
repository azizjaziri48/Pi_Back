package com.example.pi_back.Services;


import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private PartnerRepository partnerRepository;

    @Autowired
    private EmailSenderService service;
    @Override
    public List<Offer> retrieveAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer AddOffer(Offer offer)  {

          return  offerRepository.save(offer);
    }

    @Override
    public void removeOffer(int idOffer) {
      offerRepository.deleteById(idOffer);
    }

    @Override
    public Offer retrieveOffer(int idOffer) {
        return offerRepository.findById(idOffer).orElse(null);
    }

    @Override
    public Offer updateOffer(Offer offer) {
        return offerRepository.save(offer);
    }
    @Override
    public Offer assignPartnerToOffer(Integer idOffer, Integer idPartner){
        Offer offer=offerRepository.findById(idOffer).orElse(null);
        Partner partner=partnerRepository.findById(idPartner).orElse(null);
        offer.setPartner(partner);
        return offerRepository.save(offer);
    }

    @Override
    public List<Offer> findByPartner_Name(String name) {
        return offerRepository.findByPartner_Name(name);
    }

    @Override
    public List<Offer> historiqueOffers(int idUser) {
        return offerRepository.findByUsers_Id(idUser);
    }



}
