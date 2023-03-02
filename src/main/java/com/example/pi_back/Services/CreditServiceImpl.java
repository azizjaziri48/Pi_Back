package com.example.pi_back.Services;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Repositories.CreditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class CreditServiceImpl implements CreditService{

    private CreditRepository creditRepository;
    @Override
    public List<Credit> retrieveAllCredit() {
        return creditRepository.findAll();
    }

    @Override
    public Credit AddCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    @Override
    public void removeCredit(int idCredit) {
        creditRepository.deleteById(idCredit);
    }

    @Override
    public Credit retrieveCredit(int idCredit) {
        return creditRepository.findById(idCredit).orElse(null);
    }

    @Override
    public Credit updateCredit(Credit credit) {
        return creditRepository.save(credit);
    }

}
