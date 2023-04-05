package com.example.pi_back.Services;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.InternalServiceRepository;
import com.example.pi_back.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class InternalSServiceImpl implements InternalSService{
    private InternalServiceRepository internalServiceRepository;
    private UserRepository userRepository;

    @Override
    public List<InternalService> retrieveAllIService() {
        return internalServiceRepository.findAll();
    }

    @Override
    public InternalService AddIService(InternalService Iservice) {

        LocalDate d2 = Iservice.getDate();
        LocalDate d = LocalDate.now();
        if (d2.isAfter(d)) {
       internalServiceRepository.save(Iservice);
        }
        return Iservice;
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

    @Override
    public List<InternalService> ShowEventByActivitySector(Integer iduser) {
        User user= userRepository.findById(iduser).get();
        return findByActivitySector(user.getActivitySector());
    }
    @Override
    public List<InternalService> findByActivitySector(String activitySector){
   return internalServiceRepository.findByActivitySector(activitySector);
    }
    @Override
   public List<InternalService> getEventsWithin24Hours(){
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        return internalServiceRepository.findByEventDateBetween(now,tomorrow);
    }
    @Override
   public List<InternalService> getArchiveEvent(){
        LocalDate now =LocalDate.now();
        List<InternalService> internalServicesArchive= new ArrayList<>();
        List<InternalService> internalServiceList=internalServiceRepository.findAll();
        for (InternalService event : internalServiceList){
            if(event.getDate().isBefore(now)){
                internalServicesArchive.add(event);
            }
        }
        return internalServicesArchive;
   }
   @Override
   public InternalService findInternalServiceByCapacite(int capacite){
        return internalServiceRepository.findInternalServiceByCapacite(capacite);
   }
}
