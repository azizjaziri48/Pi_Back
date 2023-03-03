package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.User;
import com.example.pi_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/User")
public class UserRestController {
    private UserService UserService;
    @GetMapping("/all")
    List<User> retrieveAllUser() {
        return UserService.retrieveAllUser();
    }
    @PostMapping("/add")
    ResponseEntity<String> AddUser(@RequestBody User user){
        /*TODO email duplication handling*/
        if(!(UserService.retrieveUser(user.getId())==null)){
            return new ResponseEntity<>("Already Existing Id", HttpStatus.BAD_REQUEST);
        }

        UserService.AddUser(user);
        return new ResponseEntity<>("User added sucessfully", HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> removeUser (@PathVariable("id") Integer idUser){
        if(UserService.retrieveUser(idUser)==null){
            return new ResponseEntity<>("The user to be deleted dosen't exist", HttpStatus.NOT_FOUND);
        }
        UserService.removeUser(idUser);
        return new ResponseEntity<>("User was deleted sucessfully", HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    ResponseEntity<User> retrieveUser (@PathVariable("id") Integer idUser){
        User Retrieved_User=UserService.retrieveUser(idUser);
        if(Retrieved_User==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Retrieved_User, HttpStatus.OK);

    }
    @PutMapping("/update")
    ResponseEntity<String> updateUser (@RequestBody User user){
        if(UserService.retrieveUser(user.getId())==null){
            return new ResponseEntity<>("User Dosen't exist", HttpStatus.BAD_REQUEST);
        }
        UserService.updateUser(user);
        return new ResponseEntity<>("User updated sucessfully", HttpStatus.OK);
    }

}
