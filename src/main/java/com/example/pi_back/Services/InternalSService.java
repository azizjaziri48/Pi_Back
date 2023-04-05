package com.example.pi_back.Services;

import com.example.pi_back.Entities.InternalService;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface InternalSService {
    List<InternalService> retrieveAllIService();
    InternalService AddIService (InternalService Iservice) throws GeneralSecurityException, IOException;
    void removeIService (int idIService);
    InternalService retrieveIService (int idIService);
    InternalService updateIService(InternalService Iservice);
    List<InternalService> findByActivitySector(String activitySector);

    List<InternalService> ShowEventByActivitySector(Integer iduser);
    List<InternalService> getEventsWithin24Hours();

    List<InternalService> getArchiveEvent();
    InternalService findInternalServiceByCapacite(int capacite);

}
