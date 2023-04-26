package com.example.pi_back.Controllers;

import com.example.pi_back.Services.ReclamationSMSService;
import com.example.pi_back.Services.SMSrequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping(path = "RecSMS")
@AllArgsConstructor
public class ReclamationSMSController {
    private final ReclamationSMSService smsService;

    @PostMapping
    public String sendSMS(@RequestBody SMSrequest request) throws MalformedURLException {
        return smsService.sendSMS(request.getSmsnumber(),request.getSmsmessage());
    }

}
