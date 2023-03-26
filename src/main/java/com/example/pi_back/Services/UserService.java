package com.example.pi_back.Services;

import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> retrieveAllUser();
    User AddUser (User user);
    void removeUser (int idUser);
    User retrieveUser (int idUser);
    User updateUser (User user);
    User assignUserToOffer(Integer  idUser, Integer idOffer);
}
