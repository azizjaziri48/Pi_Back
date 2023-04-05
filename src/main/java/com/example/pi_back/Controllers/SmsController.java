package com.example.pi_back.Controllers;


import com.example.pi_back.Services.RegistrationService;
import com.example.pi_back.Services.SmsService;
import com.example.pi_back.utils.RegistrationRequest;
import com.example.pi_back.utils.SMSrequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "SMS")
@AllArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping
    public String register(@RequestBody SMSrequest request) {
        return smsService.sendSMS(request.getSmsnumber(),request.getSmsmessage());
    }


}
