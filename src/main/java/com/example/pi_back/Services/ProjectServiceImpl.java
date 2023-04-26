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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {


    public Project retrieveProjectById(int id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.orElse(null);
    }

    @Override
    public float calculateRateOfInves(int id) {
        Project p = projectRepository.findById(id).orElse(null);
        float Amount = p.getAmountinvestment();
        double Rate = 0.0;
        if (Amount != 0.0) {
            Rate = 0.12 * (1 - Math.exp(-(Amount) / 10000));
        }

        p.setTauxinvest((float) Rate);
        return (float) Rate;
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



    @Override
    public void removeProject(int idProject) {
        projectRepository.deleteById(idProject);
    }

    @Override
    public Project retrieveProject(int idProject) {
        return projectRepository.findById(idProject).orElse(null);
    }





    @Override
    public List<Project> getAlllProject() {
        return projectRepository.findAll();    }


//calcul du taux


    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();    }

    //taux 2eme methode


 /*   @Override
    public Project addProject(Project project, Long idFund) {
        BigDecimal amount = project.getAmountinvestment();
    /*formule de taux economique (invalide)
    i.setTauxInves((i.getAmoutInvestesment())/(pib*100));
    Le taux d'inves est variable selon le montant choisit
    Plus le montant aug plus le taux aug
    Minimum du montant investit = 7000
    */

     /*   if (amount.compareTo(BigDecimal.valueOf(7000)) >= 0) {
            Fund f = fundRepository.findById(idFund).orElse(null);
            //Si le montant atteint 200000 le tau reste fixe à 12%
            BigDecimal rate = BigDecimal.ZERO;
            if (amount.compareTo(BigDecimal.valueOf(200000)) < 0) {
                rate = new BigDecimal(0.12).multiply(BigDecimal.ONE.subtract(new BigDecimal(Math.exp(amount.negate().divide(new BigDecimal(10000), RoundingMode.HALF_UP).doubleValue()))));
            } else {
                rate = BigDecimal.valueOf(0.12);
            }
            project.setTauxinvest(rate);
            project.setFund(f);
            //incrémentation du fund pour chaque investissement
            f.setAmountFund(f.getAmountFund().add(project.getAmountinvestment()));
            //incrémentation du taux pour chaque investissement
            List<Project> listProj = (List<Project>) projectRepository.findAll();
            BigDecimal s = BigDecimal.ZERO;
            for (Project inv : listProj) {
                s = s.add(inv.getAmountinvestment());
            }
            s = s.add(project.getAmountinvestment());
            BigDecimal pourc_inv = project.getAmountinvestment().divide(s, 2, RoundingMode.HALF_UP);
            f.setTauxFund(pourc_inv.multiply(project.getTauxinvest()).add(BigDecimal.ONE.subtract(pourc_inv).multiply(f.getTauxFund())));
            projectRepository.save(project);
        }
        return project;
    }

*/

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

    @Override
    public List<Project> retrieveProjectbyFund(Long idFund) {
        return (List<Project>) projectRepository.retrieveProjectbyFund(idFund);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();    }

    @Override
    public List<Project> retrieveAlllProject()  {
        return projectRepository.findAll();
    }



     Integer riskscore;


    @Override
    public double getTotalInvestmentAmount() {
        List<Project> projects = projectRepository.findAll();
        double totalInvestment = 0;
        for (Project project : projects) {
            totalInvestment += project.getAmountinvestment();
        }
        return totalInvestment;
    }

    @Override
    public Project updateProject(Project project) {
        float Amount = project.getAmountinvestment();
        double Rate = 0.12 * (1 - Math.exp(-(Amount) / 10000));

        project.setName(project.getName());
        project.setDescription(project.getDescription());
        project.setAmountinvestment(Amount);
        project.setTauxinvest((float) Rate);
        project.setCountry(project.getCountry());
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


    @Scheduled(cron = "0 0 0 31 12 *")
    @Override
    public void finalAmount() {
        List<Project> listProj = (List<Project>) projectRepository.findAll();
        for (Project p : listProj) {
            p.setFinalamount((p.getAmountinvestment() * (1 + p.getTauxinvest())));
            projectRepository.save(p);
        }
    }

    //taux 2eme methode
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

        if (Amount >= 7000) {
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

    public void calculateRiskScore(String category, LocalDate startdate, LocalDate enddate, String country, float amountinvestment) {
        int riskscore = 0;
        // Ajouter des points de risque en fonction de la catégorie du projet
        if (category.equals("Animals")) {
            riskscore += 5;
        } else if (category.equals("Ecology")) {
            riskscore += 3;
        } else if (category.equals("Woman")) {
            riskscore += 4;
        }

        // Ajouter des points de risque en fonction de la durée du projet
        long days = ChronoUnit.DAYS.between(startdate, enddate);
        if (days > 365) {
            riskscore += 5;
        } else if (days > 180) {
            riskscore += 3;
        } else if (days > 90) {
            riskscore += 2;
        }

        // Ajouter des points de risque en fonction du montant d'investissement
        if (amountinvestment > 100000) {
            riskscore += 5;
        } else if (amountinvestment > 50000) {
            riskscore += 3;
        } else if (amountinvestment > 10000) {
            riskscore += 1;
        }

        // Ajouter des points de risque en fonction de la localisation du projet
        if (country.equals("Tunis")) {
            riskscore += 3;
        } else if (country.equals("France")) {
            riskscore += 2;
        } else if (country.equals("America")) {
            riskscore += 1;
        }

        this.riskscore = riskscore;
    }
    @Override
    public List<Project> getAlProject() {
        return projectRepository.findAll();    }
    @Scheduled(cron = "0 0 0 * * *") // exécuté tous les jours à minuit
    public void removeExpiredProjects() {
        LocalDate currentDate = LocalDate.now();
        List<Project> expiredProjects = projectRepository.findByEnddateBefore(currentDate);
        if (!expiredProjects.isEmpty()) {
            projectRepository.deleteAll(expiredProjects);
        }
    }

    @Override
    public Project findProjectByName(String name) {
        return projectRepository.findByName(name);
    }


}









