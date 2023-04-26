package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Notification;
import com.example.pi_back.Services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
