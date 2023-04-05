package com.example.pi_back.Services;
import com.example.pi_back.Entities.Account;
import com.example.pi_back.Services.EmailService;

import com.example.pi_back.Entities.Transaction;
import com.example.pi_back.Entities.TransactionStatus;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.AccountRepository;
import com.example.pi_back.Repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;


import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;
    private AccountRepository accountRepository;
    @Autowired
      private   EmailService emailService ;
    @Autowired
    public TransactionServiceImpl(AccountService accountService, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;

    }

    @Override
    public List<Transaction> retrieveAllTransaction() {
        return transactionRepository.findAll();
    }


       @Override
       @Transactional
       public boolean AddTransaction(Transaction transaction) {
           // Vérifier l'existence des RIB source et destinataire dans les comptes
           Account sourceAccount = accountRepository.findByRIB(transaction.getRIB_source());
           Account recipientAccount = accountRepository.findByRIB(transaction.getRIB_recipient());
           if (sourceAccount == null || recipientAccount == null) {
               return false;
           }

           // Vérifier que le solde du compte source est suffisant pour effectuer la transaction
           if (sourceAccount.getSolde() < transaction.getAmount()) {
               transaction.setStatus(TransactionStatus.REJETE); // set transaction status to "REJETE"
               transactionRepository.save(transaction);
               return false;
           }

           // Effectuer la transaction et mettre à jour les soldes des comptes source et destinataire
           sourceAccount.setSolde(sourceAccount.getSolde() - transaction.getAmount());
           recipientAccount.setSolde(recipientAccount.getSolde() + transaction.getAmount());
           transaction.setStatus(TransactionStatus.APPROUVE);
           transaction.setAccount(sourceAccount);
           sourceAccount.getTransactions().add(transaction);
           accountRepository.save(sourceAccount);
           accountRepository.save(recipientAccount);
           transactionRepository.save(transaction);
           // Envoyer un e-mail de notification à l'utilisateur
           User user = sourceAccount.getUser();
           String subject = "Notification de transaction";
           String message = "Bonjour " + user.getFirstname() + ",\n\nNous vous informons que votre transaction a été effectué avec succès d'un montant de " + transaction.getAmount() + " euros.\n\nCordialement,\nL'équipe de notre banque.";
           emailService.sendEmail(user.getEmail(), subject, message);

          return true; // kenet lekhra

       }



















    /*@Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }*/

    @Override
    public Transaction retrieveTransaction(int idTransaction) {
        return transactionRepository.findById(idTransaction).orElse(null);
    }

    @Override //nvv
    public void removeTransaction(int idTransaction) {
        Transaction transaction = transactionRepository.findById(idTransaction).orElseThrow(() -> new RuntimeException("Transaction introuvable"));
        Account source = transaction.getAccount();
        Account recipient = accountService.retrieveAccountByRIB(Long.valueOf(String.valueOf(transaction.getRIB_recipient())));


        // Annulation de la transaction : remboursement de la source, retrait du destinataire
        source.setSolde(source.getSolde() + transaction.getAmount());
        recipient.setSolde(recipient.getSolde() - transaction.getAmount());
        transactionRepository.delete(transaction);
    }





    @Override
    public Map<Integer, Integer> getTransactionCountByAccount() {
        Map<Integer, Integer> transactionCountByAccount = new HashMap<>();
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            List<Transaction> transactions = transactionRepository.findByAccount(account);
            int transactionCount = transactions.size();
            transactionCountByAccount.put(account.getId(), transactionCount);
        }
        return transactionCountByAccount;
    }



   @Override
   public Map<String, String> getTransactionStatistics() {
       Map<Integer, Integer> transactionCountByAccount = getTransactionCountByAccount();
       int totalAccountCount = transactionCountByAccount.size();
       int loyalAccountCount = 0;
       int riskyAccountCount = 0;
       for (Integer accountId : transactionCountByAccount.keySet()) {
           int transactionCount = transactionCountByAccount.get(accountId);
           Account account = accountRepository.findById(accountId).orElse(null);
           if (account != null) {
               if (transactionCount > 10) {
                   loyalAccountCount++;
               } else {
                   riskyAccountCount++;
               }
           }
       }
       double loyalAccountPercentage = (double)loyalAccountCount / totalAccountCount * 100.0;
       double riskyAccountPercentage = (double)riskyAccountCount / totalAccountCount * 100.0;

       // Format the percentages as strings with a '%' symbol at the end
       DecimalFormat df = new DecimalFormat("0.##");
       String loyalAccountPercentageStr = df.format(loyalAccountPercentage) + "%";
       String riskyAccountPercentageStr = df.format(riskyAccountPercentage) + "%";

       Map<String, String> transactionStatistics = new HashMap<>();
       transactionStatistics.put("loyalAccountPercentage", loyalAccountPercentageStr);
       transactionStatistics.put("riskyAccountPercentage", riskyAccountPercentageStr);
       return transactionStatistics;
   }



    //recherche










}
