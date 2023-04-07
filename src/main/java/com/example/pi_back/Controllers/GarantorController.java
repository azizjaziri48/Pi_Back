package com.example.pi_back.Controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pi_back.Entities.Garantor;
import com.example.pi_back.Services.IGarantorService;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Garantor")
public class GarantorController {


    @Autowired
    IGarantorService Garantorservice;


    // http://localhost:8083/pi_back/Garantor/retrieve-all-Garantor
    @GetMapping("/retrieve-all-Garantor")
    @ResponseBody
    public List<Garantor> getGarantor() {
        List<Garantor> listGarantors = Garantorservice.retrieveAllGarantors();
        return listGarantors;
    }

    //http://localhost:8083/pi_back/Garantor/retrieve-Garantor/1
    @GetMapping("/retrieve-Garantor/{Garantor-id}")
    @ResponseBody
    public Garantor retrieveGarantor(@PathVariable("Garantor-id") Long GarantorId) {
        return Garantorservice.retrieveGarantor(GarantorId);
    }

    //http://localhost:8083/pi_back/Garantor/add-Garantor/1
    @PostMapping("/add-Garantor/{Garantor-Id_user}")
    @ResponseBody
    public Garantor addGarantor(@RequestBody Garantor c,@PathVariable("Garantor-Id_user") Long Id_user)
    {
        Garantor Garantor = Garantorservice.addGarantor(c,Id_user);
        return Garantor;
    }

    //http://localhost:8083/pi_back/Garantor/modify-Garantor
    @PutMapping("/modify-Garantor")
    @ResponseBody
    public Garantor modifyGarantor(@RequestBody Garantor Garantor) {
        return Garantorservice.updateGarantor(Garantor);
    }

    //http://localhost:8083/pi_back/Garantor/remove-Garantor/81
    @DeleteMapping("/remove-Garantor/{Garantor-id}")
    @ResponseBody
    public void removeGarantor(@PathVariable("Garantor-id") Long GarantorId) {
        Garantorservice.DeleteGarantor(GarantorId);
    }
}
