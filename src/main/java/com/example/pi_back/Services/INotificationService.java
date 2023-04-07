package com.example.pi_back.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pi_back.Entities.Notification;



public interface INotificationService {
    List<Notification> retrieveAllNotifications();

    Notification addNotification();

    void deleteNotification(Long idNotification);

    //Notification updateNotification(Notification N , Long idCredit);

    Notification retrieveNotification(Long idNotification);

    //List<Notification> retrieveNotificationByClient(Long idClient);


}
