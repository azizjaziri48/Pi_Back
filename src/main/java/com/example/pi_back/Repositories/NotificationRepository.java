package com.example.pi_back.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pi_back.Entities.Notification;
@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {



    @Query("SELECT  n FROM Notification n  WHERE n.credit.user.id= :cl")
    List<Notification> getNotificationByUser(@Param("cl") Long idUser);

}
