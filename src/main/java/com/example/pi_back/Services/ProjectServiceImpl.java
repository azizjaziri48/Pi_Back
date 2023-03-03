package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private ProjectRepository projectRepository;
    @Override
    public List<Project> retrieveAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public Project AddProject(Project project) {
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
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

}