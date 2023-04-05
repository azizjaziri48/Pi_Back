package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Reclamation;
import com.example.pi_back.Services.ReclamationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pi_back.Entities.Subject;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Reclamation")

public class ReclamationRestController {
    @Autowired
    private ReclamationService ReclamationService;

    @GetMapping("/all")
    List<Reclamation> retrieveAllReclamation() {
        return ReclamationService.retrieveAllReclamation();
    }

    //@PostMapping("/add") //NV
    // Reclamation AddReclamation (@RequestBody Reclamation reclamation)  { return ReclamationService.AddReclamation(reclamation);}

    @PostMapping("/reclamations")
    public ResponseEntity<Reclamation> AddReclamation(@RequestBody Reclamation reclamation) {
        try {
            Reclamation newReclamation = ReclamationService.AddReclamation(reclamation);
            return new ResponseEntity<>(newReclamation, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update")
    Reclamation updateReclamation(@RequestBody Reclamation reclamation) {
        return ReclamationService.updateReclamation(reclamation);
    }

    @GetMapping("/get/{id}")
    public Reclamation retrieveReclamation(@PathVariable("id") int idReclamation) {
        return ReclamationService.retrieveReclamation(idReclamation);
    }

    @DeleteMapping("/delete/{id}")
    void removeReclamation(@PathVariable("id") Integer idReclamation) {
        ReclamationService.removeReclamation(idReclamation);
    }


    @GetMapping("/filterBySubject/{subject}")
    public List<Reclamation> filterBySubject(@PathVariable Subject subject) {
        return ReclamationService.filterReclamationsBySubject(subject);
    }


    //  permettre à l'admin de répondre à une réclamation donnée
    @PostMapping("/reclamation/{id}/respond")
    public ResponseEntity<?> respondToReclamation(@PathVariable(value = "id") int idReclamation,
                                                  @RequestBody String reponse) {
        Reclamation updatedReclamation = ReclamationService.respondToReclamation(idReclamation, reponse);
        return ResponseEntity.ok(updatedReclamation);
    }

 /*   @GetMapping("/{id}/status")
    public String getReclamationStatus(@PathVariable("id") int idReclamation) {
        Reclamation reclamation = ReclamationService.retrieveReclamation(idReclamation);
        if (reclamation.getEtat() == null) {
            return "En attente";
        } else if (reclamation.getEtat()) {
            return "Traitée";
        } else {
            return "Non traitée";
        }
    }*/


    @GetMapping("/{id}/status")
    public ResponseEntity<String> getReclamationStatus(@PathVariable int id) {
        String status = ReclamationService.getReclamationStatus(id);

        if (status == null) {
            return new ResponseEntity<>("La réclamation n'existe pas", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
    }







}











