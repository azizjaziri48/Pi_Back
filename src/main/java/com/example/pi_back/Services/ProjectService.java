package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;

import java.util.List;

public interface ProjectService {
        Project add(Project project);
        Project edit(Project project);
        List<Project> selectAll();
        Project selectById(int ProjectId);
        void deleteById(int ProjectId);

        void delete(Project project);

        List<Project> addAll(List<Project> list);

        void deleteAll(List<Project> list);
}







/*
        List<Project> retrieveAllProject();

        Project addProject(Project p);

        void deleteProject(int id);

        Project updateProject(Project p);

        Project retrieveProject(int id);

    }


/*
    List<Project> retrieveAllProject();
    Project AddProject (Project project);
    void removeProject (int idProject);
    Project retrieveProject (int idProject);
    Project updateProject(Project project);

    Project addProject(Project project);

    void deleteProject(int id);
}*/
