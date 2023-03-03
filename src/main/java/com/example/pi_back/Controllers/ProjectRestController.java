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
public class ProjectRestController {
    private ProjectService ProjectService;
    @GetMapping("/all")
    List<Project> retrieveAllProject() {
        return ProjectService.retrieveAllProject();
    }
    @PostMapping("/add")
    ResponseEntity<String> AddProject(@RequestBody Project Project){
        /*TODO email duplication handling*/
        if(!(ProjectService.retrieveProject(Project.getId())==null)){
            return new ResponseEntity<>("Already Existing Id", HttpStatus.BAD_REQUEST);
        }

        ProjectService.AddProject(Project);
        return new ResponseEntity<>("Project added sucessfully", HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> removeProject (@PathVariable("id") Integer idProject){
        if(ProjectService.retrieveProject(idProject)==null){
            return new ResponseEntity<>("The Project to be deleted doesn't exist", HttpStatus.NOT_FOUND);
        }
        ProjectService.removeProject(idProject);
        return new ResponseEntity<>("Project was deleted sucessfully", HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    ResponseEntity<Project> retrieveProject (@PathVariable("id") Integer idProject){
        Project Retrieved_Project=ProjectService.retrieveProject(idProject);
        if(Retrieved_Project==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Retrieved_Project, HttpStatus.OK);

    }
    @PutMapping("/update")
    ResponseEntity<String> updateProject (@RequestBody Project Project){
        if(ProjectService.retrieveProject(Project.getId())==null){
            return new ResponseEntity<>("Project Doesn't exist", HttpStatus.BAD_REQUEST);
        }
        ProjectService.updateProject(Project);
        return new ResponseEntity<>("Project updated sucessfully", HttpStatus.OK);
    }

}