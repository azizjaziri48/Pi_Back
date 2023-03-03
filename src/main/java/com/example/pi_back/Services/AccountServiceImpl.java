package com.example.pi_back.Services;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository;
    @Override
    public List<Account> retrieveAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public Account AddAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void removeAccount(int idAccount) {
        accountRepository.deleteById(idAccount);
    }

    @Override
    public Account retrieveAccount(int idAccount) {
        return accountRepository.findById(idAccount).orElse(null);
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

}
