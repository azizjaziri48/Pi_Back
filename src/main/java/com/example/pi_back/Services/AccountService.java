package com.example.pi_back.Services;

import com.example.pi_back.Entities.Account;

import java.util.List;

public interface AccountService {
    Account addAccouunt(Account account, int iduser);


    List<Account> retrieveAllAccount();
    Account AddAccount (Account account);
    void removeAccount (int idAccount);
    Account retrieveAccount (int idAccount);
    Account updateAccount (Account account);




}
