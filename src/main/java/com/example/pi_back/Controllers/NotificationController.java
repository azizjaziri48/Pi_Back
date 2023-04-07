package com.example.pi_back.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pi_back.Entities.Notification;
import com.example.pi_back.Services.INotificationService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")


@RequestMapping("/Notification")
public class NotificationController {


    @Autowired
    INotificationService notifservice;


    // http://localhost:8084/pi_back/Notification/retrieve-all-notification

    @GetMapping("/retrieve-all-notification")
    @ResponseBody
    public List<Notification> getAllnotif() {
        List<Notification> listnotif = notifservice.retrieveAllNotifications();
        return  listnotif ;
    }

    // http://localhost:8084/pi_back/Notification/retrieve-notification/1
    @GetMapping("/retrieve-notification/{notification-id}")
    @ResponseBody
    public Notification retrieveNotification(@PathVariable("notification-id") Long notifId) {
        return notifservice.retrieveNotification(notifId);
    }

    //@Scheduled(fixedRate = 180000)
    // http://localhost:8084/pi_back/Notification/add-notification/1
    @PostMapping("/add-notification")
    @ResponseBody
    public Notification addNotif()
    {
        return notifservice.addNotification();
    }



}
