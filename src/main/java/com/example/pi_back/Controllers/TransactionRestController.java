package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Transaction;
import com.example.pi_back.Services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Transaction")

public class TransactionRestController {
    private TransactionService transactionService;
    @GetMapping("/all")
    List<Transaction> retrieveAllTransaction() {return transactionService.retrieveAllTransaction();}

    @PostMapping("/add")
    Transaction AddTransaction (@RequestBody Transaction transaction){ return transactionService.AddTransaction(transaction);}

    @PutMapping("/update")
    Transaction updateTransaction(@RequestBody Transaction transaction){return transactionService.updateTransaction(transaction);}

    @GetMapping("/get/{id}")
    public Transaction retrieveTransaction(@PathVariable("id") int idTransaction) {return transactionService.retrieveTransaction(idTransaction);}

    @DeleteMapping("/delete/{id}")
    void removeTransaction(@PathVariable("id") Integer idTransaction) {transactionService.removeTransaction(idTransaction);
    }







}
