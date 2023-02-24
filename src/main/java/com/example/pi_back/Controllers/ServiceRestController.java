package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Service;
import com.example.pi_back.Services.ServiceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/service")
public class ServiceRestController {
    private ServiceService serviceService;
    @GetMapping("/all")
    List<Service> retrieveAllService() {
        return serviceService.retrieveAllService();
    }
    @PostMapping("/add")
    Service AddService (@RequestBody Service service){
        return serviceService.AddService(service);
    }
    @DeleteMapping("/delete/{id}")
    void removeService (@PathVariable("id") Integer idService){
        serviceService.removeService(idService);
    }
    @GetMapping("/get/{id}")
    Service retrieveService (@PathVariable("id") Integer idService){
       return serviceService.retrieveService(idService);
    }
    @PutMapping("/update")
    Service updateService(@RequestBody Service service){
        return serviceService.updateService(service);
    }
}
