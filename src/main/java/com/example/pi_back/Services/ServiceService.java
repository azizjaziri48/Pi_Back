package com.example.pi_back.Services;
import com.example.pi_back.Entities.Service;

import java.util.List;

public interface ServiceService {
    List<Service> retrieveAllService();
    Service AddService (Service service);
    void removeService (int idService);
    Service retrieveService (int idService);
    Service updateService(Service service);
}
