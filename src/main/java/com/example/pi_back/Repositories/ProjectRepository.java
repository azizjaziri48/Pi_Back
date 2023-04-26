package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
  @Query(value = "SELECT  i  FROM Project i WHERE i.fund.idFund= ?1")
    List<Project> retrieveProjectbyFund(Long idFund);

    Project findByName(String projectName);

    public default List<String> findDistinctCategories() {
        List<String> categories = new ArrayList<>();
        for (Project project : findAll()) {
            if (!categories.contains(project.getCategory())) {
                categories.add(project.getCategory());
            }
        }
        return categories;
    }
    List<Project> findByEnddateBefore(LocalDate endDate);

}
