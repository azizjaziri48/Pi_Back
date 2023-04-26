package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.HistoricalData;
import com.example.pi_back.Entities.Project;
import com.example.pi_back.Entities.SimulationResult;
import com.example.pi_back.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SimulationController {

    @Autowired
    private ProjectRepository projectRepository;

   /* @GetMapping("/projects/simulate/{id}/{investmentAmount2}")
    public SimulationResult simulate(@PathVariable int id,@PathVariable int investmentAmount2) {
        BigDecimal investmentAmount = new BigDecimal(investmentAmount2);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id " + id));

        List<HistoricalData> historicalDataList = new ArrayList<>();
        historicalDataList.add(new HistoricalData(BigDecimal.valueOf(0.05), LocalDate.of(2020, 1, 1)));
        historicalDataList.add(new HistoricalData(BigDecimal.valueOf(0.03), LocalDate.of(2020, 2, 1)));
        historicalDataList.add(new HistoricalData(BigDecimal.valueOf(-0.02), LocalDate.of(2020, 3, 1)));
        historicalDataList.add(new HistoricalData(BigDecimal.valueOf(0.01), LocalDate.of(2020, 4, 1)));
        historicalDataList.add(new HistoricalData(BigDecimal.valueOf(0.02), LocalDate.of(2020, 5, 1)));
        BigDecimal simulatedReturn = calculateSimulatedReturn(historicalDataList, project.getTauxinvest());

        BigDecimal finalAmount = investmentAmount.add(simulatedReturn);

        SimulationResult result = new SimulationResult();
        result.setProjectId(project.getId());
        result.setProjectName(project.getName());
        result.setInvestmentAmount(investmentAmount);
        result.setSimulatedReturn(simulatedReturn);
        result.setFinalAmount(finalAmount);

        return result;
    }*/

    private BigDecimal calculateSimulatedReturn(List<HistoricalData> historicalDataList, BigDecimal tauxinvest) {
        BigDecimal simulatedReturn = BigDecimal.ZERO;
        for (HistoricalData historicalData : historicalDataList) {
            BigDecimal rate = historicalData.getRate();
            LocalDate date = historicalData.getDate();
            BigDecimal factor = BigDecimal.ONE.add(rate);
            BigDecimal weightedFactor = factor.multiply(tauxinvest);
            simulatedReturn = simulatedReturn.add(weightedFactor);
        }
        return simulatedReturn;
    }
}