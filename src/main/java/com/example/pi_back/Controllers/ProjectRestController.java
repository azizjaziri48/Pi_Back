package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.ProjectRepository;
import com.example.pi_back.Services.ProjectService;
import com.example.pi_back.Services.ProjectServiceImpl;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
//import io.swagger.annotations.Api;

@RestController
@AllArgsConstructor
@RequestMapping("/Project")
public class ProjectRestController
{
    private ProjectService projectService;
    @PostMapping("/ajouterProject")
    public ResponseEntity<String>  add(@RequestBody Project project){

       projectService.add(project);
        return ResponseEntity.ok("Added successfully !");

    }
    @PostMapping("ajouterAllProject")
    public ResponseEntity<String> addAll(@RequestBody List<Project> list){

      projectService.addAll(list);
        return ResponseEntity.ok("AddedAll successfully !");

    }
    @PutMapping("/ModifierProject")
    public ResponseEntity<String> edit(@RequestBody Project project){

        projectService.edit(project);
        return ResponseEntity.ok("Updated successfully !");

    }
    @DeleteMapping("SupprimerProject")
    public ResponseEntity<String> delete(@RequestBody Project project){
        projectService.delete(project);
        return ResponseEntity.ok("Deleted successfully !");


    }
    @GetMapping("/afficherProject")
    public List<Project> afficher(){return projectService.selectAll();}

    @GetMapping("/afficherProjectAvecId/{id}")
    public Project afficherOfferAvecId(@PathVariable int id)
    {
        return projectService.selectById(id);
    }

    @DeleteMapping("/deleteProjectById")
    public ResponseEntity<String> deleteProjectById(@RequestBody int id)
    {
       projectService.deleteById(id);
        return ResponseEntity.ok("Deleted successfully !");
    }
}




/* @Autowired
    ProjectServiceImpl ProjectService;

    @GetMapping("/retrieve-all-Projects")
    @ResponseBody
    public List<Project> getProject() {
        List<Project> list = this.ProjectService.retrieveAllProject();
        return list;
    }

    @GetMapping("/retrieve-Project/{Project-id}")
    @ResponseBody
    public Project retrieveProject(@PathVariable("Project-id") int Id) {
        return this.ProjectService.retrieveProject(Id);
    }

*/
/*  @PostMapping({"/add-Project"})
    @ResponseBody
    public Project addProject(@RequestBody Project p) {
       // Project Project = this.ProjectService.addProject(p);
        return ProjectService.addProject(p);
}
*/
/*
    @DeleteMapping("/remove-Project/{Project-id}")
    @ResponseBody
    public void removeProject(@PathVariable("Project-id") int Id) {
        this.ProjectService.deleteProject(Id);
    }

    @PutMapping("/modify-Project")
    @ResponseBody
    public Project modifyProject(@RequestBody Project project) {
        return this.ProjectService.updateProject(project);
    }


}
/*
import java.util.List;*/
/*
@RestController
@AllArgsConstructor


@RequestMapping("/project")
public class ProjectRestController {
    private ProjectService projetService;
    @GetMapping("/all")
    List<Project> retrieveAllProject() {
        return projetService.retrieveAllProject();
    }
    @PostMapping("/add")
    @ResponseBody
    Project AddProject (@RequestBody Project project){
        return projetService.AddProject(project);
    }


    @DeleteMapping("/delete/{id}")
    void removeProject (@PathVariable("id") Integer idProject){
        projetService.removeProject(idProject);
    }
    @GetMapping("/get/{id}")
    Project retrieveProject (@PathVariable("id") Integer idProject){
        return projetService.retrieveProject(idProject);
    }
    @PutMapping("/update")
    Project updateProject(@RequestBody Project project){
        return projetService.updateProject(project);
    }}
*/


