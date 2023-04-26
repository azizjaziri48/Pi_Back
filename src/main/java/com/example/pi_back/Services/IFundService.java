package com.example.pi_back.Services;

import com.example.pi_back.Entities.Fund;

import java.util.List;


public interface IFundService {


    List<Fund> retrieveAllFunds();

    Fund addFund(Fund f);

    void deleteFund(Long idFund);

    Fund updateFund(Fund fun);

    Fund retrieveFund(Long idFund);

}
