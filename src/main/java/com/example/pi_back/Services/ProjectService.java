package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;
import org.springframework.stereotype.Service;

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

        float CalculateRateOfInves(int id);

        double Rate(float AmountInvestment);



        Project addProject(Project project, Long idFund);


    List<Project> retrieveAllProjectSortedAndSearched(String sortBy, String searchBy);

    List<Project> retrieveAllProjectSorted(String sortBy);

    List<Project> retrieveAllProjectSearched(String searchBy);
}







