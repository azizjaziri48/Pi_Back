package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Investor;
import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.InvestorRepository;
import com.example.pi_back.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/investor")
public class InvestorController {
    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping("/addinvestisseur")
    public ResponseEntity<String> addInvestor(@RequestBody Investor investor) {
        investorRepository.save(investor);
        return ResponseEntity.ok("Investor " + investor.getName() + " added successfully");
    }
    @PostMapping("/{investorId}/participate/{projectId}")
    public ResponseEntity<String> participateInProject(@PathVariable int investorId, @PathVariable int projectId) {
        Optional<Investor> investorOptional = investorRepository.findById(investorId);
        if (investorOptional.isPresent()) {
            Investor investor = investorOptional.get();
            Optional<Project> projectOptional = projectRepository.findById(projectId);
            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                investor.getInvestedProjects().add(project);
                investorRepository.save(investor);
                return ResponseEntity.ok("Investor " + investor.getName() + " has participated in project " + project.getName());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project with id " + projectId + " not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Investor with id " + investorId + " not found");
        }
    }
}
