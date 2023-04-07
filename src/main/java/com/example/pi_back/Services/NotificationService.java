package com.example.pi_back.Services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pi_back.Entities.User;
//import com.example.pi_back.Entities.Complaint;
import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.Notification;
import com.example.pi_back.Repositories.UserRepository;
import com.example.pi_back.Repositories.CreditRepository;
import com.example.pi_back.Repositories.NotificationRepository;


@Service

public class NotificationService implements INotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    CreditRepository creditRepo;

    @Autowired
    UserRepository userRepository;



    @Override
    public List<Notification> retrieveAllNotifications() {

        return (List<Notification>) notificationRepository.findAll();
    }





    @Override
    //@Scheduled(cron="*/5 * * * * *")
    public Notification addNotification () {

        System.out.println("hola");



        long miliseconds = System.currentTimeMillis();
        Date date = new Date();

        //long diff = ChronoUnit.DAYS.between(date, dAfter);


        System.out.println("test");
        Iterable<Credit> C = creditRepo.findAll();
        for(Credit t : C)
        {
            ZoneId defaultZoneId = ZoneId.systemDefault();
            //System.out.println(date);

            //Date d =t.getMonthlyPaymentDate();

            Instant instant = date.toInstant();
            //Instant instant1 = t.getMonthlyPaymentDate().toInstant();


            // Date date11 = Calendar.getInstance().getTime();

            //LocalDate dBefore = LocalDate.parse("2022-03-21", DateTimeFormatter.ISO_LOCAL_DATE);


            System.out.println("test2");
            LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
            LocalDateTime localDate1 = Instant.ofEpochMilli(t.getMonthlyPaymentDate().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            System.out.println("test3");

            if (ChronoUnit.DAYS.between(localDate,localDate1)==2)
            {
                Notification N = new Notification();
                N.setObject("you have two days left to pay your payment");
                //System.out.println("test4");
                N.setCredit(t);
                N.setDateNotif(date);

                notificationRepository.save(N);
                return N ;

                //System.out.println("test5");
            }

        };
        return null;

        // N.setCredit(C);
        //Date todaysDate = LocalDate.now();

    }

    @Override
    public void deleteNotification(Long idNotification) {
        notificationRepository.deleteById(idNotification);
    }

    //@Override
	/*public Notification updateNotification(Notification N , Long idCredit) {
		Credit C = creditRepo.findById(idCredit).orElse(null);
		 N.setCredit(C);

		return  notificationRepository.save(N);
	}
	*/

    @Override
    public Notification retrieveNotification(Long idNotification) {
        Notification N= notificationRepository.findById(idNotification) .orElse(null) ;
        return N;
    }

    /*public List<Notification> retrieveNotificationByClient(Long idClient) {
        User cl = clientRepository.findById(idClient).orElse(null);
        List<Notification> not = notificationRepository.getNotificationByClient(idClient);

        return not ;
    }*/


}
