package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Repositories.InternalServiceRepository;
import com.example.pi_back.Services.EmailSenderService;
import com.example.pi_back.Services.InternalSService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;

import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/Iservice")
public class IServiceRestController  {
    private InternalSService internalSService;
    private InternalServiceRepository internalServiceRepository;
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
    public void sendEventReminderEmails() throws MessagingException, IOException {
        List<InternalService> events = internalSService.getEventsWithin24Hours();
        for (InternalService event : events) {
            File file = ResourceUtils.getFile("src/main/java/com/example/pi_back/reminderevent.html");
            System.out.println("File Found : " + file.exists());
            String content = new String(Files.readAllBytes(file.toPath()));
            content = content.replace("${event}", event.getName());
            service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn",content,"Event reminder !");
            //service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn","vous participez a un evenement demain !","reminder event "+event.getName());
        }
    }
    @GetMapping("/getArchive")
    public List<InternalService> getArchive(){
        return internalSService.getArchiveEvent();
    }
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
    @GetMapping("/events/cancel/{eventid}")
    public ResponseEntity<String> cancelEvent(@PathVariable("eventid") Integer eventId) throws IOException, MessagingException {
        Optional<InternalService> optionalEvent = internalServiceRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            InternalService event = optionalEvent.get();
            internalServiceRepository.delete(event);
            File file = ResourceUtils.getFile("src/main/java/com/example/pi_back/eventcancel.html");
            System.out.println("File Found : " + file.exists());
            String content = new String(Files.readAllBytes(file.toPath()));
            content = content.replace("${event}", event.getName());
            service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn",content,"event canceled ! "+event.getName());
            //emailSenderService.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn"," Bonjour Ceci est une annulation  pour l'événement "+ event.getNameEvent() +"  qui aura lieu le "+ event.getDateEvent() +"\n\n"+"Cordialement,L'équipe de microfinance.","reminder event "+event.getNameEvent());
            return ResponseEntity.ok("Event has been cancelled successfully");
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }
    @GetMapping("/events/reported/{eventid}")
    public ResponseEntity<String> reportedEvent(@PathVariable("eventid") Integer eventId) throws IOException, MessagingException {
        Optional<InternalService> optionalEvent = internalServiceRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            InternalService event = optionalEvent.get();
            event.setDate(event.getDate().plusDays(10));
            internalServiceRepository.save(event);
            File file = ResourceUtils.getFile("src/main/java/com/example/pi_back/eventreported.html");
            System.out.println("File Found : " + file.exists());
            String content = new String(Files.readAllBytes(file.toPath()));
            content = content.replace("${date}", event.getDate().toString());
            content = content.replace("${name}", event.getName());
            service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn",content,"reminder event "+event.getName());
            return ResponseEntity.ok("Event has been reported successfully");
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }
}
