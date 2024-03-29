package com.example.pi_back.Services;

import com.example.pi_back.Entities.Account;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface AccountService {
    List<Account> retrieveAllAccount();
    Account AddAccount (Account account);
    void removeAccount (int idAccount);
    Account retrieveAccount (int idAccount);
    Account updateAccount (Account account);
    Account assignEventToAccount(int idAccount,int idIService);

}
