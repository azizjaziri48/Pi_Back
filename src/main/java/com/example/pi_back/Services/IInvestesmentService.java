package com.example.pi_back.Services;

import com.example.pi_back.Entities.Investesment;

import java.util.List;


public interface IInvestesmentService {
    List<Investesment> retrieveAllInvestesments();

    Investesment addInvestesment(Investesment i , Long idFund);

    Investesment updateInvestesment(Investesment i);

    Investesment retrieveInvestesment(Long cinInvestesment);

    void CalculateAmoutOfInves(Long idInvestesment);

    float CalculateRateOfInves(Long idInvestissement);

    void finalAmount();


}
