package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Service
public interface ProjectService {


    double getTotalInvestmentAmount();

    List<Project> retrieveAllProject();
        List<Project> getProject();

        Project AddProject (Project project, long idFund);
        void removeProject (int idProject);
        Project retrieveProject (int idProject);
        Project updateProject(Project project);

        void CalculateAmountOfInves(int id);


        List<Project> getAlllProject();

        void finalAmount();


    //calcul du taux

    double Rate(float AmountInvestment);



        Project addProject(Project project, Long idFund);


    List<Project> retrieveAllProjectSortedAndSearched(String sortBy, String searchBy);

    List<Project> retrieveAllProjectSorted(String sortBy);

    List<Project> retrieveAllProjectSearched(String searchBy);

    List<Project> retrieveProjectbyFund(Long idFund);

    List<Project> findAll();

    List<Project> retrieveAlllProject();

    void calculateRiskScore(String category, LocalDate startdate, LocalDate enddate, String country, float amountinvestment);

    List<Project> getAlProject();



    Project findProjectByName(String projectName);

    Project retrieveProjectById(int id);

    float calculateRateOfInves(int idProject);

    List<Project> getAllProjects();
}







