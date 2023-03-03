package com.example.pi_back.Services;


import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Repositories.PartnerRepository;
import com.example.pi_back.Repositories.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceServiceImpl implements ServiceService{
    private ServiceRepository serviceRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public List<com.example.pi_back.Entities.Service> retrieveAllService() {
        return serviceRepository.findAll();
    }

    @Override
    public com.example.pi_back.Entities.Service AddService(com.example.pi_back.Entities.Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public void removeService(int idService) {
         serviceRepository.deleteById(idService);
    }

    @Override
    public com.example.pi_back.Entities.Service retrieveService(int idService) {
        return serviceRepository.findById(idService).orElse(null);
    }

    @Override
    public com.example.pi_back.Entities.Service updateService(com.example.pi_back.Entities.Service service) {
        return serviceRepository.save(service);
    }
    @Override
public com.example.pi_back.Entities.Service assignPartnerToService(Integer idservice, Integer idPartner){
        com.example.pi_back.Entities.Service service=serviceRepository.findById(idservice).orElse(null);
        Partner partner=partnerRepository.findById(idPartner).orElse(null);
        service.setPartner(partner);
        return serviceRepository.save(service);
}
}
