package com.example.pi_back.Services;

import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    @Override
    public List<User> retrieveAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User AddUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeUser(int idUser) {
        userRepository.deleteById(idUser);
    }

    @Override
    public User retrieveUser(int idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
