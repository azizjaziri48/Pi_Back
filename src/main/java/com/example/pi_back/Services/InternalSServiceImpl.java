package com.example.pi_back.Services;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Repositories.InternalServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class InternalSServiceImpl implements InternalSService{
    private InternalServiceRepository internalServiceRepository;
    @Override
    public List<InternalService> retrieveAllIService() {
        return internalServiceRepository.findAll();
    }

    @Override
    public InternalService AddIService(InternalService Iservice) {
        return internalServiceRepository.save(Iservice);
    }


    @Override
    public void removeIService(int idIService) {
      internalServiceRepository.deleteById(idIService);
    }

    @Override
    public InternalService retrieveIService(int idIService) {
        return internalServiceRepository.findById(idIService).orElse(null);
    }

    @Override
    public InternalService updateIService(InternalService Iservice) {
        return internalServiceRepository.save(Iservice);
    }
}
