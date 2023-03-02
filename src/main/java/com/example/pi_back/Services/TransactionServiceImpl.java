package com.example.pi_back.Services;


import com.example.pi_back.Entities.Transaction;
import com.example.pi_back.Repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;


    @Override
    public List<Transaction> retrieveAllTransaction() {return transactionRepository.findAll();}

    @Override
    public Transaction AddTransaction(Transaction transaction) {return  transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {return  transactionRepository.save(transaction);
    }

    @Override
    public Transaction retrieveTransaction(int idTransaction) {return transactionRepository.findById(idTransaction).orElse(null);}

    @Override
    public void removeTransaction(int idTransaction) {transactionRepository.deleteById(idTransaction);}
}
