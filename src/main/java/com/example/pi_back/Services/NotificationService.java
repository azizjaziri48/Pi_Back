package com.example.pi_back.Services;

import com.example.pi_back.Entities.IntevAge;
import com.example.pi_back.Entities.Project;
import com.example.pi_back.Entities.User;

import java.util.List;

public interface NotificationService {

    void sendNotification(Project project);

    List<User> getUsersByIntevAge(IntevAge invetage);
}
