package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Services.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/partner")
public class PartnerRestController {
    private PartnerService partnerService;
    @GetMapping("/all")
    List<Partner> retrieveAllPartners() {
        return partnerService.retrieveAllPartners();
    }
    @PostMapping("/add")
    Partner AddPartner (@RequestBody Partner partner){
    return partnerService.AddPartner(partner);
    }
    @DeleteMapping("/delete/{id}")
    void removePartner (@PathVariable("id") Integer idPartner){
    partnerService.removePartner(idPartner);
    }
    @GetMapping("/get/{id}")
    Partner retrievePartner (@PathVariable("id") Integer idPartner){
    return partnerService.retrievePartner(idPartner);
    }
    @PutMapping("/update")
    Partner updatePartner(@RequestBody Partner partner){
        return partnerService.updatePartner(partner);
    }

}
