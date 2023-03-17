package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Investment;
import com.example.pi_back.Services.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Investment")
public class InvestmentController {
	@Autowired
	InvestmentService InvestmentService;





	// http://localhost:8083/BKFIN/Investment/retrieve-all-Investments
			@GetMapping("/retrieve-all-Investments")
			@ResponseBody
			public List<Investment> getInvestment() {
			List<Investment> listInvestment = InvestmentService.retrieveAllinvestments();
			return listInvestment;
			} 
			

			// http://localhost:8083/BKFIN/Investment/retrieve-Investment/1
			@GetMapping("/retrieve-Investment/{Investment-id}")
			@ResponseBody
			public Investment retrieveInvestment(@PathVariable("Investment-id") Long InvestmentId) {
			return InvestmentService.retrieveinvestment(InvestmentId);
			}
			
			

						/*@GetMapping("/retrieve-Investments-by-fund/{Fund-id}")
						@ResponseBody
						public List<Investment> getInvestmentbyFund(@PathVariable("Fund-id") Long idFund) {
						List<Investment> listInvestment = InvestmentService.retrie(idFund);
						return listInvestment;
						} */
			



	@PostMapping("/add_Investment/{Fund-id}")
	ResponseEntity<String> AddInvestment(@RequestBody Investment i,@PathVariable("Fund-id") Long idFund){
		if(!(InvestmentService.retrieveinvestment(i.getIdInvestment())==null)){
			return new ResponseEntity<>("Already Existing Id", HttpStatus.BAD_REQUEST);
		}

		InvestmentService.addinvestment(i,idFund);
		return new ResponseEntity<>("Investment added successfully", HttpStatus.CREATED);

	}
			
						@PutMapping("/modify-Investment")
						@ResponseBody
						public Investment modifyInvestment(@RequestBody Investment i) {
						return InvestmentService.updateinvestment(i);}
			
			/*@GetMapping("/export")
			public void exportToPDF(HttpServletResponse response) throws DocumentException,IOException {
				response.setContentType("application/pdf");
				DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
				String currentDateTime = dateFormater.format(new Date());
				String headerKey = "Content-Disposition"; 
				String headerValue = "Attachement;filename=inves_"+ currentDateTime + ".pdf";
				response.setHeader(headerKey, headerValue);
				List<Investment> listInvestment = InvestmentService.retrieveAllInvestments();
				investPDFExporter exporter = new investPDFExporter(listInvestment);
				exporter.export(response);
			} */
			
			@GetMapping("/CalculateAmoutOfInves/{Investment-id}")
			@ResponseBody
						public void CalculateAmoutOfInves(@PathVariable("Investment-id") Long idInvestment) {
						 InvestmentService.CalculateAmountOfInves(idInvestment);
						}
			
			@GetMapping("/finalAmount")
			@ResponseBody
						public void finalAmount() {
						InvestmentService.finalAmount();
						}
						
			@GetMapping("/CalculateRateOfInves/{Investment-id}")
			@ResponseBody
						public float CalculateRateOfInves(@PathVariable("Investment-id") Long idInvestment) {
						return InvestmentService.CalculateRateOfInves(idInvestment);
						}
						@GetMapping("/Rate/{amount}")
						@ResponseBody
									public double Rate(@PathVariable("amount") float AmountInvestestesment) {
									return InvestmentService.Rate(AmountInvestestesment);
									}
						
}
