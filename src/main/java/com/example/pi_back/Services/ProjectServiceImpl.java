package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@AllArgsConstructor
@Service

public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    @Override
    public Project add(Project p) {
        return projectRepository.save(p);
    }
    @Override

    public Project edit(Project p) {
        return projectRepository.save(p);
    }

    @Override
    public List<Project> selectAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project selectById(int Id) {
        return projectRepository.findById(Id).get();
    }

    @Override
    public void deleteById(int Id) {
        projectRepository.deleteById(Id);
    }

    @Override
    public void delete(Project p) {

       projectRepository.delete(p);
    }

    @Override
    public List<Project> addAll(List<Project> list) {
        return projectRepository.saveAll(list);
    }

    @Override
    public void deleteAll(List<Project> list) {
        projectRepository.deleteAll(list);
    }

}






   /* @Autowired
   private ProjectRepository projectRepository;

    public ProjectServiceImpl() {
    }

    public List<Project> retrieveAllProject() {
        return this.projectRepository.findAll();
    }

    public Project addProject(Project p) {
        this.projectRepository.save(p);
        return p;
    }



    public void deleteProject(int id) {
        this.projectRepository.deleteById(id);
    }

    public Project updateProject(Project p) {
        this.projectRepository.save(p);
        return p;
    }

    public Project retrieveProject(int id) {
        Project project = (Project)this.projectRepository.findById(id).orElse((Project) null);
        return project;
    }
}
    /*private ProjectRepository projectRepository;

    @Override
    public List<com.example.pi_back.Entities.Project> retrieveAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public com.example.pi_back.Entities.Project AddProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void removeProject(int idProject) {
        projectRepository.deleteById(idProject);
    }

    @Override
    public com.example.pi_back.Entities.Project retrieveProject(int idProject) {
        return projectRepository.findById(idProject).orElse(null);
    }

    @Override
    public com.example.pi_back.Entities.Project updateProject(com.example.pi_back.Entities.Project project) {
        return projectRepository.save(project);
    }

}*/