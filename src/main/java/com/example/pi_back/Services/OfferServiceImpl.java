package com.example.pi_back.Services;

import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Repositories.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    @Override
    public List<Offer> retrieveAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer AddOffer(Offer offer) {
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
}
