package com.example.pi_back.Controllers;

//import com.example.emailsender.Sesource.EmailMessage;
//import com.example.emailsender.service.EmailSenderService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

   /* private final EmailSenderService emailSenderService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {
        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }*/
}
