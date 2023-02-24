package com.example.pi_back.Services;

import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Repositories.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class PartnerServiceImpl implements PartnerService {
    private PartnerRepository partnerRepository;
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
}
