package com.example.pi_back.Services;

import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Repositories.InternalServiceRepository;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PartnerServiceImpl implements PartnerService {
    private PartnerRepository partnerRepository;
    private final InternalServiceRepository internalServiceRepository;

    @Override
    public List<Partner> retrieveAllPartners() {
        return partnerRepository.findAll();
    }

    @Override
    public Partner AddPartner(Partner partner) {
        return partnerRepository.save(partner);
    }

    @Override
    public void removePartner(int idPartner) {
     partnerRepository.deleteById(idPartner);
    }

    @Override
    public Partner retrievePartner(int idPartner) {
        return partnerRepository.findById(idPartner).orElse(null);
    }

    @Override
    public Partner updatePartner(Partner partner) {
        return partnerRepository.save(partner);
    }
   @Override
    public Partner findMostFrequentPartner(){
        List<Partner> partnerList =partnerRepository.findMostFrequentPartner();
       return partnerList.get(0);
    }
    @Override
    public Partner findLessFrequentPartner(){
        List<Partner> partnerList=partnerRepository.findLessFrequentPartner();
        return partnerList.get(0);
}
    @Override
    public  List<Partner> findPartnersWithoutOffers(){
        return partnerRepository.findPartnersWithoutOffers();
    }

    @Override
    public Long countPartnersWithOffers() {
        return partnerRepository.countPartnersWithOffers();
    }
    @Override
   public double getPartnersOfferPercentages() {
       Long nbPartnersWithOffers =partnerRepository.countPartnersWithOffers();
       Long nbPartnersWithoutOffers = partnerRepository.count() - nbPartnersWithOffers;
       Long totalPartners = partnerRepository.count();
       double pourcentageWithoutOffers = (double) nbPartnersWithoutOffers / totalPartners * 100.0;
       return pourcentageWithoutOffers;
    }
    @Override
    public double getPartnersOfferPercentages1(){
        Long nbPartnersWithOffers =partnerRepository.countPartnersWithOffers();
        Long totalPartners = partnerRepository.count();
        double pourcentageWithOffers = (double) nbPartnersWithOffers / totalPartners * 100.0;
        return pourcentageWithOffers;
    }
}
