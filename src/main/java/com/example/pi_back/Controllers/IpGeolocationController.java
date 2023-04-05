package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Services.IpService;
import com.example.pi_back.Services.SmsService;
import com.example.pi_back.utils.SMSrequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping(path = "ip")
@AllArgsConstructor
public class IpGeolocationController {
    private final IpService ipService;
    @GetMapping("/currentip")
    public String getip(@RequestBody SMSrequest request) throws MalformedURLException {
        return ipService.GetIP();
    }
}
