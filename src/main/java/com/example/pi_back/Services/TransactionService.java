package com.example.pi_back.Services;


import com.example.pi_back.Entities.Transaction;

import java.util.List;

public interface TransactionService {
    public List<Transaction> retrieveAllTransaction();

    public Transaction AddTransaction (Transaction transaction);

    public Transaction updateTransaction (Transaction transaction);

    public Transaction retrieveTransaction(int idTransaction);

    public  void removeTransaction(int idTransaction);


}
