package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Investesment;
import com.example.pi_back.Services.InvestesmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Investesment")
public class InvestesmentController {
    @Autowired
    InvestesmentService investesmentService;


    // http://localhost:8083/pi_back/Investesment/retrieve-all-investesments
    @GetMapping("/retrieve-all-investesments")
    @ResponseBody
    public List<Investesment> getInvestesment() {
        List<Investesment> listInvestesment = investesmentService.retrieveAllInvestesments();
        return listInvestesment;
    }


    // http://localhost:8083/pi_back/Investesment/retrieve-investesment/1
    @GetMapping("/retrieve-investesment/{investesment-id}")
    @ResponseBody
    public Investesment retrieveInvestesment(@PathVariable("investesment-id") Long investesmentId) {
        return investesmentService.retrieveInvestesment(investesmentId);
    }


    // http://localhost:8083/pi_back/Investesment/retrieve-investesments-by-fund/1
    @GetMapping("/retrieve-investesments-by-fund/{Fund-id}")
    @ResponseBody
    public List<Investesment> getInvestesmentbyFund(@PathVariable("Fund-id") Long idFund) {
        List<Investesment> listInvestesment = investesmentService.retrieveInvestesmentbyFund(idFund);
        return listInvestesment;
    }

    // http://localhost:8083/pi_back/Investesment/add_investesment/1
    @PostMapping("/add_investesment/{Fund-id}")
    @ResponseBody
    public Investesment addInvestesment(@RequestBody Investesment i,@PathVariable("Fund-id") Long idFund)
    {
        System.out.println(idFund);
        Investesment investesment = investesmentService.addInvestesment(i,idFund);
        return investesment;
    }

    // http://localhost:8083/pi_back/Investesment/modify-investesment/
    @PutMapping("/modify-investesment")
    @ResponseBody
    public Investesment modifyInvestesment(@RequestBody Investesment i) {
        return investesmentService.updateInvestesment(i);}

    //http://localhost:8083/pi_back/Investesment/export
   /* @GetMapping("/export")
    public void exportToPDF(HttpServletResponse response) throws DocumentException,IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
        String currentDateTime = dateFormater.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "Attachement;filename=inves_"+ currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<Investesment> listInvestesment = investesmentService.retrieveAllInvestesments();
        investPDFExporter exporter = new investPDFExporter(listInvestesment);
        exporter.export(response);
    } */

    // http://localhost:8083/pi_back/Investesment/CalculateAmoutOfInves/1
    @GetMapping("/CalculateAmoutOfInves/{Investesment-id}")
    @ResponseBody
    public void CalculateAmoutOfInves(@PathVariable("Investesment-id") Long idInvestesment) {
        investesmentService.CalculateAmoutOfInves(idInvestesment);
    }

    // http://localhost:8083/pi_back/Investesment/finalAmount
    @GetMapping("/finalAmount")
    @ResponseBody
    public void finalAmount() {
        investesmentService.finalAmount();
    }

    // http://localhost:8083/pi_back/Investesment/CalculateRateOfInves/3/2
    @GetMapping("/CalculateRateOfInves/{Investesment-id}")
    @ResponseBody
    public float CalculateRateOfInves(@PathVariable("Investesment-id") Long idInvestesment) {
        return investesmentService.CalculateRateOfInves(idInvestesment);
    }
    // http://localhost:8083/pi_back/Investesment/Rate/8000
    @GetMapping("/Rate/{amount}")
    @ResponseBody
    public double Rate(@PathVariable("amount") float AmountInvestestesment) {
        return investesmentService.Rate(AmountInvestestesment);
    }
}
