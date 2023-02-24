package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Services.InternalSService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Iservice")
public class IServiceRestController  {
    private InternalSService internalSService;
    @GetMapping("/all")
    List<InternalService> retrieveAllIService() {
        return internalSService.retrieveAllIService();
    }
    @PostMapping("/add")
    InternalService AddIService (@RequestBody InternalService Iservice){
        return internalSService.AddIService(Iservice);
    }
    @DeleteMapping("/delete/{id}")
    void removeIService (@PathVariable("id") Integer idIService){
        internalSService.removeIService(idIService);
    }
    @GetMapping("/get/{id}")
    InternalService retrieveIService (@PathVariable("id") Integer idIService){
        return internalSService.retrieveIService(idIService);
    }
    @PutMapping("/update")
    InternalService updateService(@RequestBody InternalService Iservice){
        return internalSService.updateIService(Iservice);
    }
}
