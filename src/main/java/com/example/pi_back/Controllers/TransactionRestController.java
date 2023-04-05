package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Transaction;
import com.example.pi_back.Services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/Transaction")

public class TransactionRestController {
    private TransactionService transactionService;
    @GetMapping("/all")
    List<Transaction> retrieveAllTransaction() {return transactionService.retrieveAllTransaction();}

   @PostMapping("/add")
   public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
       boolean success = transactionService.AddTransaction(transaction);
       if(success) {
           return ResponseEntity.ok("Transaction added successfully.");
       } else {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction failed. Invalid RIB or insufficient funds.");
       }
   }
    //@PutMapping("/update")
   // Transaction updateTransaction(@RequestBody Transaction transaction){return transactionService.updateTransaction(transaction);}

    @GetMapping("/get/{id}")
    public Transaction retrieveTransaction(@PathVariable("id") int idTransaction) {return transactionService.retrieveTransaction(idTransaction);}

    @DeleteMapping("/delete/{id}")
    void removeTransaction(@PathVariable("id") Integer idTransaction) {transactionService.removeTransaction(idTransaction);
    }



//nvvv
@GetMapping("/transactionCountByAccount")
public Map<Integer, Integer> getTransactionCountByAccount() {
    return transactionService.getTransactionCountByAccount();
}

    @GetMapping("/transactionStatistics")
    public Map<String, String> getTransactionStatistics() {
        return transactionService.getTransactionStatistics();
    }









}
