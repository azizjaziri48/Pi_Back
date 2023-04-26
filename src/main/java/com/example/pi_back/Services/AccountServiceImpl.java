package com.example.pi_back.Services;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.Transaction;
import com.example.pi_back.Entities.TypeAccount;
import com.example.pi_back.Repositories.AccountRepository;
import com.example.pi_back.Repositories.InternalServiceRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@EnableScheduling
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private AccountRepository accountRepository;

    @Autowired
    private TaskScheduler taskScheduler;


    @Autowired
    private   EmailService emailService ;

    @Autowired
    private EntityManager entityManager;
    private final InternalServiceRepository internalServiceRepository;
    @Override
    public List<Account> retrieveAllAccount() {
        List<Account> accounts = entityManager.createQuery("FROM Account", Account.class).getResultList();
        for (Account account : accounts) {
            Hibernate.initialize(account.getTransactions());
        }
        return accounts;
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

    @Override //nv
    public Account retrieveAccountByRIB(Long rib) {
        return accountRepository.findByRIB(rib);
    }

    public double calculateTransactionFee(Transaction transaction) {
        double transactionFee = 0.0;

        // Récupération du compte associé à la transaction
        Account account = transaction.getAccount();

        // Récupération du type de compte associé au compte
        TypeAccount typeAccount = account.getTypeaccount();

        // Calcul des frais de transaction en fonction du type de compte
        if (typeAccount == TypeAccount.CURRENT) {
            transactionFee = transaction.getAmount() * 0.01; // Frais de 1% pour les comptes courants
        } else if (typeAccount == TypeAccount.SAVING) {
            transactionFee = transaction.getAmount() * 0.005; // Frais de 0.5% pour les comptes d'épargne
        }

        return transactionFee;
    }

    @Override //hedhi heya eli tekhdem jawha behi
    public void deleteAllDeletableAccounts(int inactiveMinutes) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(inactiveMinutes);
        LocalDateTime threshold = now.minus(duration);

        // Désactiver les contraintes de clé étrangère
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();

        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            if (account.getSolde() == 0 && (account.getLastUpdateDate() == null || account.getLastUpdateDate().isBefore(threshold))) {
                accountRepository.delete(account);
            }
        }

        // Réactiver les contraintes de clé étrangère
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
    }

    // @Scheduled(fixedDelay = 300000) // every 5 minutes
    @Scheduled(cron = "0 * * * * *")
    public void autoDeleteAccounts() {
        deleteAllDeletableAccounts(5);
    }

}
