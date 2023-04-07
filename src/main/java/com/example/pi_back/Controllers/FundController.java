package com.example.pi_back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Services.FundService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Fund")

public class FundController {

    @Autowired
    FundService fundService;

    // http://localhost:8083/pi_back/Fund/retrieve-all-funds
    @GetMapping("/retrieve-all-funds")
    @ResponseBody
    public List<Fund> getFund() {
        List<Fund> listFund = fundService.retrieveAllFunds();
        return listFund;
    }

    // http://localhost:8083/pi_back/Fund/retrieve-fund/1
    @GetMapping("/retrieve-fund/{fund-id}")
    @ResponseBody
    public Fund retrieveFund(@PathVariable("fund-id") Long fundId) {
        return fundService.retrieveFund(fundId);
    }

    // http://localhost:8083/pi_back/Fund/add-fund
    @PostMapping("/add-fund")
    @ResponseBody
    public Fund addFund(@RequestBody Fund f)
    {
        Fund fund = fundService.addFund(f);
        return fund;
    }


    // http://localhost:8083/pi_back/Fund/remove-fund/3
    @DeleteMapping("/remove-fund/{fund-id}")
    @ResponseBody
    public void removeFund(@PathVariable("fund-id") Long fundId) {
        fundService.deleteFund(fundId);
    }

    // http://localhost:8083/pi_back/Fund/modify-fund
    @PutMapping("/modify-fund")
    @ResponseBody
    public Fund modifyFund(@RequestBody Fund fund) {
        return fundService.updateFund(fund);
    }

}