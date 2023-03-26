package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Services.EmailSenderService;
import com.example.pi_back.Services.InternalSService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/Iservice")
public class IServiceRestController  {
    private InternalSService internalSService;
    @Autowired
    private EmailSenderService service;
    @GetMapping("/all")
    List<InternalService> retrieveAllIService() {
        return internalSService.retrieveAllIService();
    }
    @PostMapping("/add")
    InternalService AddIService (@RequestBody InternalService Iservice) throws GeneralSecurityException, IOException {
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
    //afficher la liste des évenements selon le secteur d'activité d'un user
    @GetMapping("/getEventByUser/{id}")
    @Transactional
     List<InternalService> ShowEventByActivitySector(@PathVariable("id") Integer iduser) {
        return internalSService.ShowEventByActivitySector(iduser);
    }
    //afficher les évenements dont la durée est de 24h
    @GetMapping("/getEventsWithin24Hours")
    List<InternalService> getEventsWithin24Hours(){
        return internalSService.getEventsWithin24Hours();
    }
    @Scheduled(fixedRate = 60 * 60 * 1000) // exécute toutes les heures
   // @Scheduled(cron="*/30 * * * * *")
    public void sendEventReminderEmails() {
        List<InternalService> events = internalSService.getEventsWithin24Hours();
        for (InternalService event : events) {
            service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn","vous participez a un evenement demain !","reminder event "+event.getName());
        }
    }
    @GetMapping("/getArchive")
    public List<InternalService> getArchive(){
        return internalSService.getArchiveEvent();
    }
}
