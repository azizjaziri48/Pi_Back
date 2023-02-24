package com.example.pi_back.Services;

import com.example.pi_back.Entities.InternalService;

import java.util.List;

public interface InternalSService {
    List<InternalService> retrieveAllIService();
    InternalService AddIService (InternalService Iservice);
    void removeIService (int idIService);
    InternalService retrieveIService (int idIService);
    InternalService updateIService(InternalService Iservice);
}
