package com.example.pi_back.Services;

import com.example.pi_back.Entities.IntevAge;
import com.example.pi_back.Entities.Project;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.pi_back.Entities.IntevAge.*;

@Service
public class NotificationServiceImpl implements NotificationService {

        @Autowired
        private UserRepository userRepository;
        @Override

        public void sendNotification(Project project) {
            Set<User> users = project.getUsers();
            for (User user : users) {
            }
        }

    @Override
    public List<User> getUsersByIntevAge(IntevAge invetage) {
        List<User> users = new ArrayList<>();
        // Code to retrieve users from the database based on their age range
        return users;    }


        private void sendNotificationToUser(User user, Project project) {
            // Code pour envoyer la notification à l'utilisateur
            // Utilisez ici une bibliothèque de notification ou un service de messagerie tiers.
            // Par exemple, vous pouvez utiliser Spring Boot's mail ou Twilio API pour envoyer un SMS.
            System.out.println("Notification envoyée à l'utilisateur " + user.getFirstname() + " pour le projet " + project.getName());
        }
    }
