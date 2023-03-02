package com.example.pi_back.Services;

import com.example.pi_back.Entities.Credit;

import java.util.List;

public interface CreditService {
    List<Credit> retrieveAllCredit();
    Credit AddCredit (Credit credit);
    void removeCredit (int idCredit);
    Credit retrieveCredit (int idCredit);
    Credit updateCredit (Credit credit);
}
