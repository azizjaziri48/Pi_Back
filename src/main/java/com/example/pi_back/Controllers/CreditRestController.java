package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Services.CreditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Credit")
public class CreditRestController {

    private CreditService CreditService;
    @GetMapping("/all")
    List<Credit> retrieveAllCredit() {
        return CreditService.retrieveAllCredit();
    }
    @PostMapping("/add")
    ResponseEntity<String> AddCredit(@RequestBody Credit credit){
        if(!(CreditService.retrieveCredit(credit.getId())==null)){
            return new ResponseEntity<>("Already Existing Id", HttpStatus.BAD_REQUEST);
        }

        CreditService.AddCredit(credit);
        return new ResponseEntity<>("Credit added successfully", HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> removeCredit (@PathVariable("id") Integer idCredit){
        if(CreditService.retrieveCredit(idCredit)==null){
            return new ResponseEntity<>("The credit to be deleted does not exist", HttpStatus.NOT_FOUND);
        }
        CreditService.removeCredit(idCredit);
        return new ResponseEntity<>("Credit was deleted successfully", HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    ResponseEntity<Credit> retrieveCredit (@PathVariable("id") Integer idCredit){
        Credit Retrieved_Credit=CreditService.retrieveCredit(idCredit);
        if(Retrieved_Credit==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Retrieved_Credit, HttpStatus.OK);

    }
    @PutMapping("/update")
    ResponseEntity<String> updateCredit (@RequestBody Credit credit){
        if(CreditService.retrieveCredit(credit.getId())==null){
            return new ResponseEntity<>("Credit does not exist", HttpStatus.BAD_REQUEST);
        }
        CreditService.updateCredit(credit);
        return new ResponseEntity<>("Credit updated successfully", HttpStatus.OK);
    }

}
