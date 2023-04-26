package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Services.Amortissement;
import com.example.pi_back.Services.CreditExcelExporter;
import com.example.pi_back.Services.CreditService;
import com.example.pi_back.Services.IDuesHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Validated
@RequestMapping("/Credit")
public class CreditController {

    @Autowired
    CreditService creditservice;
    @Autowired
    IDuesHistoryService DuesHistoryservice;



    // http://localhost:8083/pi_back/Credit/retrieve-all-Credit
    @GetMapping("/retrieve-all-Credit")
    @ResponseBody
    public List<Credit> getCredit() {
        List<Credit> listCredits = creditservice.retrieveAllCredits();
        return listCredits;
    }

    //http://localhost:8083/pi_back/Credit/retrieve-Credit/1
    @GetMapping("/retrieve-Credit/{Credit-id}")
    @ResponseBody
    public Credit retrieveCredit(@PathVariable("Credit-id") Long CreditId) {
        return creditservice.retrieveCredit(CreditId);
    }

    //http://localhost:8083/pi_back/Credit/add-Credit/1/1/1
    @PostMapping("/add-Credit/{Credit-Id_user}/{Credit-Id_fund}/{credit_idgarant}")
    @ResponseBody
    public ResponseEntity<Credit> addcredit(@Valid @RequestBody Credit c,
                                            @PathVariable("Credit-Id_user") Integer Id_user,
                                            @PathVariable("Credit-Id_fund") Long Id_fund,
                                            @PathVariable("credit_idgarant") Long Id_garant,
                                            BindingResult result)
    {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        Credit Credit = creditservice.addCredit(c,Id_user,Id_fund,Id_garant);
        return ResponseEntity.ok(Credit);
    }






    //http://localhost:8083/pi_back/Credit/modify-credit/1/1
    @PutMapping("/modify-credit/{Credit-Id_user}/{Credit-Id_fund}")
    @ResponseBody
    public Credit modifycredit(@RequestBody Credit credit,@PathVariable("Credit-Id_user") Integer Id_user,
                               @PathVariable("Credit-Id_fund") Long Id_fund)
    {
        return creditservice.updateCredit(credit,Id_user,Id_fund);
    }

    //http://localhost:8083/pi_back/Credit/remove-credit/10
    @DeleteMapping("/remove-credit/{credit-id}")
    @ResponseBody
    public void removecredit(@PathVariable("credit-id") Long creditId) {
        creditservice.DeleteCredit(creditId);
    }

    //http://localhost:8083/pi_back/Credit/archive-credit/10
    @PutMapping("/archive-credit/{credit-id}")
    @ResponseBody
    public Credit modifycredit(@PathVariable("credit-id") Long idcredit) {
        return creditservice.ArchiveCredit(idcredit);
    }


    //http://localhost:8083/pi_back/Credit/calculm
    @PostMapping("/calculm")
    @ResponseBody
    public float mensualite(@RequestBody Credit c)
    {
        float Credit = creditservice.Calcul_mensualite(c);
        return Credit;
    }

    //http://localhost:8083/pi_back/Credit/CalculTabAmortissementTauxFix
    @PostMapping("/CalculTabAmortissementTauxFix")
    @ResponseBody
    public Amortissement[] TabAmortissementTauxFix(@RequestBody Credit cr)
    {
        Amortissement[] Credit = creditservice.TabAmortissementTauxFix(cr);
        //ByteArrayInputStream stream = ExcelFileExporter.contactListToExcelFile(creditservice.TabAmortissementTauxFix(cr));

        return Credit;
    }

    //http://localhost:8083/pi_back/Credit/CalculTabAmortissementAmortConstant
    @PostMapping("/CalculTabAmortissementAmortConstant")
    @ResponseBody
    public Amortissement[] TabAmortissementAmortConstant(@RequestBody Credit cr)
    {
        Amortissement[] Credit = creditservice.TabAmortissementAmortConst(cr);
        //ByteArrayInputStream stream = ExcelFileExporter.contactListToExcelFile(creditservice.TabAmortissementTauxFix(cr));

        return Credit;
    }

    //http://localhost:8083/pi_back/Credit/CalculTabAmortissementTauxVariable
    @PostMapping("/CalculTabAmortissementTauxVariable")
    @ResponseBody
    public Amortissement[] TabAmortissementTauxVariable(@RequestBody Credit cr)
    {
        Amortissement[] Credit = creditservice.TabAmortissementTauxVariable(cr);
        //ByteArrayInputStream stream = ExcelFileExporter.contactListToExcelFile(creditservice.TabAmortissementTauxVariable(cr));

        return Credit;
    }



    //http://localhost:8083/pi_back/Credit/export/excel/amortissementConstant/1
    @GetMapping("/export/excel/amortissementConstant/{idCredit}")
    @ResponseBody
    public void exportToExcelCAmortConst(HttpServletResponse response, @PathVariable("idCredit") Long idCredit) throws IOException {

        Credit cr = creditService.retrieveCredit(idCredit);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headervalue = "attachment; filename=Tableau_Credit_N_" + cr.getIdCredit() + ".xlsx";
        response.setHeader(headerKey, headervalue);

        Amortissement[] credit = creditservice.TabAmortissementAmortConst(cr);
        List<Amortissement> list = Arrays.asList(credit);
        CreditExcelExporter exp =new CreditExcelExporter(list);
        exp.export(response);
    }

    //http://localhost:8083/pi_back/Credit/export/excel/tauxFixe/1
    @GetMapping("/export/excel/tauxFixe/{idCredit}")
    @ResponseBody
    public void exportToExcelCTauxFixe(HttpServletResponse response, @PathVariable("idCredit") Long idCredit) throws IOException {

        Credit cr = creditService.retrieveCredit(idCredit);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headervalue = "attachment; filename=Tableau_Credit_N_" + cr.getIdCredit() + ".xlsx";
        response.setHeader(headerKey, headervalue);

        Amortissement[] credit = creditservice.TabAmortissementAmortConst(cr);
        List<Amortissement> list = Arrays.asList(credit);
        CreditExcelExporter exp =new CreditExcelExporter(list);
        exp.export(response);
    }



    //http://localhost:8083/pi_back/Credit/export/excel/TauxFix/10000/2/0.05
    @GetMapping("/export/excel/TauxFix/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public void exportToExcelTauxFix(HttpServletResponse response,
                              @PathVariable("mnttotl") float mnttotl,
                              @PathVariable("period") float period,
                              @PathVariable("interst") float interst)
            throws IOException {

        Credit cr=new Credit(mnttotl,period,interst);


        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headervalue = "attachment; filename=Tableau_Credit_N_"+cr.getIdCredit()+".xlsx";
        response.setHeader(headerKey, headervalue);
        Amortissement[] credit = creditservice.TabAmortissementTauxFix(cr);
        List<Amortissement> list = Arrays.asList(credit);
        CreditExcelExporter exp =new CreditExcelExporter(list);
        //CreditExcelExporter exp = new CreditExcelExporter(list);
        exp.export(response);

    }

    //http://localhost:8083/pi_back/Credit/export/excel/AmortConst/10000/2/0.05
    @GetMapping("/export/excel/AmortConst/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public void exportToExcelAmortConst(HttpServletResponse response,
                                     @PathVariable("mnttotl") float mnttotl,
                                     @PathVariable("period") float period,
                                     @PathVariable("interst") float interst)
            throws IOException {

        Credit cr=new Credit(mnttotl,period,interst);


        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headervalue = "attachment; filename=Tableau_Credit_N_"+cr.getIdCredit()+".xlsx";
        response.setHeader(headerKey, headervalue);
        Amortissement[] credit = creditservice.TabAmortissementAmortConst(cr);
        List<Amortissement> list = Arrays.asList(credit);
        CreditExcelExporter exp =new CreditExcelExporter(list);
        exp.export(response);

    }

    //http://localhost:8083/pi_back/Credit/export/excel/TauxVariable/10000/2/0.05
    @GetMapping("/export/excel/TauxVariable/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public void exportToExcelTauxVariable(HttpServletResponse response,
                              @PathVariable("mnttotl") float mnttotl,
                              @PathVariable("period") float period,
                              @PathVariable("interst") float interst)
            throws IOException {

        Credit cr=new Credit(mnttotl,period,interst);


        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headervalue = "attachment; filename=Tableau_Credit_N_"+cr.getIdCredit()+".xlsx";
        response.setHeader(headerKey, headervalue);
        Amortissement[] credit = creditservice.TabAmortissementTauxVariable(cr);
        List<Amortissement> list = Arrays.asList(credit);
        CreditExcelExporter exp =new CreditExcelExporter(list);
        //CreditExcelExporter exp = new CreditExcelExporter(list);
        exp.export(response);

    }



    //http://localhost:8083/pi_back/Credit/simulate/10000/2/0.05
    @GetMapping("/simulate/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortissement simulate(@PathVariable("mnttotl") float mnttotl,
                                  @PathVariable("period") float period,
                                  @PathVariable("interst") float interst) {
        Credit cr=new Credit(mnttotl,period,interst);

        return creditservice.Simulateur(cr);

    }

    //http://localhost:8083/pi_back/Credit/TabTauxFix/10000/2/0.05
    @GetMapping("/TabTauxFix/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortissement[]  tab(@PathVariable("mnttotl") float mnttotl,
                                @PathVariable("period") float period,
                                @PathVariable("interst") float interst) {
        Credit cr=new Credit(mnttotl,period,interst);

        return creditservice.TabAmortissementTauxFix(cr);

    }

    //http://localhost:8083/pi_back/Credit/TabTauxVariable/10000/2/0.05
    @GetMapping("/TabTauxVariable/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortissement[]  tabtauxvariable(@PathVariable("mnttotl") float mnttotl,
                                @PathVariable("period") float period,
                                @PathVariable("interst") float interst) {
        Credit cr=new Credit(mnttotl,period,interst);

        return creditservice.TabAmortissementTauxVariable(cr);

    }

    //http://localhost:8083/pi_back/Credit/InteretConstant/10000/2/0.05
    @GetMapping("/InteretConstant/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortissement[]  TabInteretConstant(@PathVariable("mnttotl") float mnttotl,
                                               @PathVariable("period") float period,
                                               @PathVariable("interst") float interst) {
        Credit cr=new Credit(mnttotl,period,interst);

        return creditservice.TabAmortissementInteretConstant(cr);

    }

    //http://localhost:8083/pi_back/Credit/AmortConst/10000/2/0.05
    @GetMapping("/AmortConst/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortissement[]  TabAmortConst(@PathVariable("mnttotl") float mnttotl,
                                               @PathVariable("period") float period,
                                               @PathVariable("interst") float interst) {
        Credit cr=new Credit(mnttotl,period,interst);

        return creditservice.TabAmortissementAmortConst(cr);

    }

    //http://localhost:8083/pi_back/Credit/InteretConst/10000/2/0.05
    @GetMapping("/InteretConst/{mnttotl}/{period}/{interst}")
    @ResponseBody
    public Amortissement[] TabAmortInteretConst(@PathVariable("mnttotl") float mnttotl,
                                                @PathVariable("period") float period,
                                                @PathVariable("interst") float interst) {
        Credit cr = new Credit(mnttotl, period, interst);

        return creditservice.TabAmortissementInteretConst(cr);

    }



    //http://localhost:8083/pi_back/Credit/tableau-amortissement/10000/0.05/2
    @GetMapping("/tableau-amortissement/{montant}/{tauxInteretAnnuel}/{dureeAnnees}")
    @ResponseBody
    public List<Map<String, Double>> tableauAmortissement(@PathVariable double montant,
                                                          @PathVariable double tauxInteretAnnuel,
                                                          @PathVariable int dureeAnnees) {
        double montantRestant = montant;
        double mensualite = Math.ceil(montant / (dureeAnnees * 12));
        return CreditService.tableauAmortissement(montantRestant, tauxInteretAnnuel, mensualite, dureeAnnees);
    }


    //http://localhost:8083/pi_back/Credit/activeCredit/3
    @GetMapping("/activeCredit/{user-id}")
    @ResponseBody
    public Credit retrieveActiveCredit(@PathVariable("user-id") Integer userid) {
        Credit cr=creditservice.retrieveActiveCredit(userid);
        if (cr.getAmount()!=999999)
            cr.setInterestRate(DuesHistoryservice.PayedAmount(cr.getIdCredit()));
        return cr;

    }

    //http://localhost:8083/pi_back/Credit/lastcredit/3
    @GetMapping("/lastcredit/{user-id}")
    @ResponseBody
    public Credit retrievelastcompletedCredit(@PathVariable("user-id") Integer userid) {
        Credit cr=creditservice.retrievelastCredit(userid);
        if (cr.getAmount()!=999999)
            cr.setInterestRate(DuesHistoryservice.PayedAmount(cr.getIdCredit()));
        return cr;

    }

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }


    //http://localhost:8083/pi_back/Credit/ApprovalRate
    @GetMapping("/ApprovalRate")
    @ResponseBody
    public double getApprovalRate() {
        return creditService.getApprovalRate();
    }

    //http://localhost:8083/pi_back/Credit/ApprovalAndCompletedRate
    @GetMapping("/ApprovalAndCompletedRate")
    @ResponseBody
    public double getApprovalAndCompletedRate() {
        return creditService.getApprovalAndCompletedRate();
    }

    //http://localhost:8083/pi_back/Credit/GoodRiskRate
    @GetMapping("/GoodRiskRate")
    @ResponseBody
    public double getGoodRiskRate() {
        return creditService.getGoodRiskRate();
    }

    //http://localhost:8083/pi_back/Credit/NormalRiskRate
    @GetMapping("/NormalRiskRate")
    @ResponseBody
    public double getNormalRiskRate() {
        return creditService.getNormalRiskRate();
    }

    //http://localhost:8083/pi_back/Credit/BadRiskRate
    @GetMapping("/BadRiskRate")
    @ResponseBody
    public double getBadRiskRate() {
        return creditService.getBadRiskRate();
    }

    //http://localhost:8083/pi_back/Credit/statsRisk
    @GetMapping("/statsRisk")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getCreditRiskStats() {
        Map<String, String> stats = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0.00");
        double goodRiskRate = creditService.getGoodRiskRate();
        double normalRiskRate = creditService.getNormalRiskRate();
        double badRiskRate = creditService.getBadRiskRate();
        double forbiddenCreditRate = 100.0 - goodRiskRate - normalRiskRate - badRiskRate;
        stats.put("Credit with good risk rate", df.format(Math.round(goodRiskRate * 100.0) / 100.0) + "%");
        stats.put("Credit with normal risk rate", df.format(Math.round(normalRiskRate * 100.0) / 100.0) + "%");
        stats.put("Credit with bad risk rate", df.format(Math.round(badRiskRate * 100.0) / 100.0) + "%");
        stats.put("Forbidden credit", df.format(Math.round(forbiddenCreditRate * 100.0) / 100.0) + "%");
        return ResponseEntity.ok(stats);
    }

    //http://localhost:8083/pi_back/Credit/statsCredit
    @GetMapping("/statsCredit")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getCreditStats() {
        Map<String, String> stats = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0.00");
        double approvedCreditsRate = creditService.getApprovalRate();
        double approvedAndCompletedCreditsRate = creditService.getApprovalAndCompletedRate();
        double forbiddenCreditRate = 100.0 - approvedCreditsRate - approvedAndCompletedCreditsRate;
        stats.put("Credit approved and not completed", df.format(Math.round(approvedCreditsRate * 100.0) / 100.0) + "%");
        stats.put("Credit approved and completed", df.format(Math.round(approvedAndCompletedCreditsRate * 100.0) / 100.0) + "%");
        stats.put("Forbidden credit", df.format(Math.round(forbiddenCreditRate * 100.0) / 100.0) + "%");
        return ResponseEntity.ok(stats);
    }




}
