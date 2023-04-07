package com.example.pi_back.Services;

import java.util.List;
import com.example.pi_back.Entities.Fund;


public interface IFundService {


    List<Fund> retrieveAllFunds();

    Fund addFund(Fund f);

    void deleteFund(Long idFund);

    Fund updateFund(Fund fun);

    Fund retrieveFund(Long idFund);

}
