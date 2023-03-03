package com.example.pi_back.Services;


import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Offer AddOffer(Offer offer) {
        service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn","l'offre "+offer.getName()+" est ajoutée","nouvelle offre");
        return offerRepository.save(offer);
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



}
