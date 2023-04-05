package com.example.pi_back.Services;


import com.example.pi_back.Entities.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {
     List<Transaction> retrieveAllTransaction();

     boolean AddTransaction (Transaction transaction); //testée

   //  Transaction updateTransaction (Transaction transaction);

    Transaction retrieveTransaction(int idTransaction);

     void removeTransaction(int idTransaction); //nvvadmin testée



//nvvv
    Map<Integer, Integer> getTransactionCountByAccount(); //testée

     Map<String, String> getTransactionStatistics(); //testée













}
