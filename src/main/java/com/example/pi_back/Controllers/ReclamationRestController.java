package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Reclamation;
import com.example.pi_back.Services.ReclamationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Reclamation")

public class ReclamationRestController {
    private ReclamationService ReclamationService;
    @GetMapping("/all")
    List<Reclamation> retrieveAllReclamation() {return ReclamationService.retrieveAllReclamation();}

    @PostMapping("/add")
    Reclamation AddReclamation (@RequestBody Reclamation reclamation){ return ReclamationService.AddReclamation(reclamation);}

    @PutMapping("/update")
    Reclamation updateReclamation(@RequestBody Reclamation reclamation){return ReclamationService.updateReclamation(reclamation);}

    @GetMapping("/get/{id}")
    public Reclamation retrieveReclamation(@PathVariable("id") int idReclamation) {return ReclamationService.retrieveReclamation(idReclamation);}

    @DeleteMapping("/delete/{id}")
    void removeReclamation(@PathVariable("id") Integer idReclamation) {ReclamationService.removeReclamation(idReclamation);
    }







}
