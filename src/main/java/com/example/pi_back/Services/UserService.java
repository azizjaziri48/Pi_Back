package com.example.pi_back.Services;

import com.example.pi_back.Entities.User;

import java.util.List;

public interface UserService {
    List<User> retrieveAllUser();
    User AddUser (User user);
    void removeUser (int idUser);
    User retrieveUser (int idUser);
    User updateUser (User user);

    List<User> getUsersByAgeRange(int lowerAge, int upperAge);

    User ajouterUser(User us, int idaccount);
}
