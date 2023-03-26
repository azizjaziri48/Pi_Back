package com.example.pi_back.Services;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Repositories.AccountRepository;
import com.example.pi_back.Repositories.InternalServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository;
    private final InternalServiceRepository internalServiceRepository;

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

    @Override
    public Account assignEventToAccount(int idAccount, int idIService) {
            Account account=accountRepository.findById(idAccount).orElse(null);
            InternalService internalService=internalServiceRepository.findById(idIService).orElse(null);
           internalService.setCapacite(internalService.getCapacite()-1);
            if(account.getInternalServices()==null){
                Set<InternalService> internalServiceSet= new HashSet<>();
                internalServiceSet.add(internalService);
                account.setInternalServices(internalServiceSet);
            }
            else{
                account.getInternalServices().add(internalService);
            }
            return  accountRepository.save(account);
    }

}
