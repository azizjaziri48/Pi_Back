package com.example.pi_back.Services;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.AccountRepository;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<User> retrieveAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User AddUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeUser(int idUser) {
        userRepository.deleteById(idUser);
    }

    @Override
    public User retrieveUser(int idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User assignUserToOffer(Integer idUser, Integer idOffer) {
        Offer offer= offerRepository.findById(idOffer).orElse(null);
        User user=userRepository.findById(idUser).orElse(null);
        Account account=accountRepository.findByUser_Id(idUser);
        //affectation avec offer
        Set<Offer> offerSet=new HashSet<>();
        offerSet.add(offer);
        user.setOffers(offerSet);
        if(account.getSolde()<=0){
            return null;
        }
        else{
            account.setSolde((long) (account.getSolde()-offer.getValeur()));
            Set<User> userSet=new HashSet<>();
            userSet.add(user);
            offer.setUsers(userSet);
            return user;
    }
    }
@Override
    public Account assignAccounttoUser(int idAccount, int idUser) {
        Account account1=accountRepository.findById(idAccount).orElse(null);
        User user1=userRepository.findById(idUser).orElse(null);
        account1.setUser(user1);
        accountRepository.save(account1);
        return account1;
    }

}
