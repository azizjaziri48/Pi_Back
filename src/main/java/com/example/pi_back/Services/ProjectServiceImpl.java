package com.example.pi_back.Services;

import com.example.pi_back.Entities.Fund;
import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.FundRepository;
import com.example.pi_back.Repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {




    @Override
    public double getTotalInvestmentAmount() {
        List<Project> projects = projectRepository.findAll();
        double totalInvestment = 0;
        for (Project project : projects) {
            totalInvestment += project.getAmountinvestment();
        }
        return totalInvestment;
    }
    @Autowired
    private final FundRepository fundRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> retrieveAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getProject() {
        return projectRepository.findAll();
    }

    @Override
    public Project AddProject(Project project, long idFund) {
        return projectRepository.save(project);
    }

  /*  @Override
    public Project AddProject(Project project, long idFund) {
        return projectRepository.save(project);
    }*/

    @Override
    public void removeProject(int idProject) {
        projectRepository.deleteById(idProject);
    }

    @Override
    public Project retrieveProject(int idProject) {
        return projectRepository.findById(idProject).orElse(null);
    }

    @Override
    public Project updateProject(Project project) {
        float Amount = project.getAmountinvestment();
        double Rate = 0.12 * (1 - Math.exp(-(Amount) / 10000));

        project.setName(project.getName());
        project.setDescription(project.getDescription());
        project.setAmountinvestment(Amount);
        project.setTauxinvest((float) Rate);
        project.setNumberinv(project.getNumberinv());
        project.setTown(project.getTown());
        project.setStartdate(project.getStartdate());
        project.setEnddate(project.getEnddate());
     //   project.setRate(project.getRate());

        return projectRepository.save(project);
    }

    @Override
    public void CalculateAmountOfInves(int id) {
        Project p = projectRepository.findById(id).orElse(null);
        p.setFinalamount(p.getAmountinvestment() + (p.getAmountinvestment() * p.getTauxinvest()));
        projectRepository.save(p);
    }

    @Override
    public List<Project> getAlllProject() {
        return projectRepository.findAll();    }

    @Scheduled(cron = "0 0 0 31 12 *")
    @Override
    public void finalAmount() {
        List<Project> listProj = (List<Project>) projectRepository.findAll();
        for (Project p : listProj) {
            p.setFinalamount((p.getAmountinvestment() * (1 + p.getTauxinvest())));
            projectRepository.save(p);
        }
    }

    @Override
    public float CalculateRateOfInves(int id) {
        Project p = projectRepository.findById(id).orElse(null);
        float Amount = p.getAmountinvestment();
        double Rate = 0.0;
        if (Amount != 0.0) {
            Rate = 0.12 * (1 - Math.exp(-(Amount) / 10000));
        }

        p.setTauxinvest((float) Rate);
        return (float) Rate;
    }

    @Override
    public double Rate(float AmountInvestment) {
        return 0.12 * (1 - Math.exp(-(AmountInvestment) / 10000));
    }

    @Override
    public Project addProject(Project project, Long idFund) {
        float Amount = project.getAmountinvestment();
		/*formule de taux economique (invalide)
		i.setTauxInves((i.getAmoutInvestesment())/(pib*100));
		Le taux d'inves est variable selon le montant choisit
		Plus le montant aug plus le taux aug
		Minimum du montant investit = 7000
		*/
        if (Amount >= 4000) {
            Fund f = fundRepository.findById(idFund).orElse(null);
            //Si le montant atteint 200000 le tau reste fixe à 12%
            double Rate = 0.12*(1- Math.exp(-(Amount)/10000));
            project.setTauxinvest((float) Rate);
            project.setFund(f);
            //incrémentation du fund pour chaque investissement
            f.setAmountFund(f.getAmountFund()+project.getAmountinvestment());
            //incrémentation du taux pour chaque investissement
            List<Project> listProj = (List<Project>) projectRepository.findAll();
            float s = 0;
            for (Project  inv : listProj) {
                s=s+(inv.getAmountinvestment());
            }
            s=s+project.getAmountinvestment();
            float pourc_inv = (project.getAmountinvestment())/s;
            f.setTauxFund(pourc_inv*(project.getTauxinvest())+(1-pourc_inv)*f.getTauxFund());
            projectRepository.save(project);
        }
        return project;
    }


    @Override
    public List<Project> retrieveAllProjectSortedAndSearched(String sortBy, String searchBy) {
        return null;
    }

    @Override
    public List<Project> retrieveAllProjectSorted(String sortBy) {
        return null;
    }

    @Override
    public List<Project> retrieveAllProjectSearched(String searchBy) {
        return null;
    }


}





