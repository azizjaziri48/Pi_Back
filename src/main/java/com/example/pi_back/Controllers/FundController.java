package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Services.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Fund")

public class FundController {
	
	@Autowired
	FundService fundService;
	
			@GetMapping("/retrieve-all-funds")
			@ResponseBody
			public List<Fund> getFund() {
		List<Fund> listFund = fundService.retrieveAllFunds();
			return listFund;
			}
			
			@GetMapping("/retrieve-fund/{fund-id}")
			@ResponseBody
			public Fund retrieveFund(@PathVariable("fund-id") Long fundId) {
			return fundService.retrieveFund(fundId);
			}

			@PostMapping("/add-fund")
			@ResponseBody
	ResponseEntity<String> AddFund(@RequestBody Fund f) {

				{
					Fund fund = fundService.addFund(f);
					return new ResponseEntity<>("Fund added successfully", HttpStatus.CREATED);
				}
			}
			
			@DeleteMapping("/remove-fund/{fund-id}")
			@ResponseBody
			public void removeFund(@PathVariable("fund-id") Long fundId) {
				fundService.deleteFund(fundId);
			}
			
			// http://localhost:8083/BKFIN/Fund/modify-fund
			@PutMapping("/modify-fund")
			@ResponseBody
			public Fund modifyFund(@RequestBody Fund fund) {
			return fundService.updateFund(fund);
			}

}
