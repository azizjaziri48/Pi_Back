package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.GeoIP;
import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Services.IpService;
import com.example.pi_back.Services.IpServiceImpl;
import com.example.pi_back.Services.SmsService;
import com.example.pi_back.utils.SMSrequest;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping(path = "ip")

public class IpGeolocationController {
    private final IpService geoIPLocationService;

    public IpGeolocationController(IpService geoIPLocationService) {
        this.geoIPLocationService = geoIPLocationService;
    }
    @GetMapping("/getip")
    public String getIp(@PathVariable String ipAddress, HttpServletRequest request
    ) {
       /* return ipService.GetIP();*/
        return "";
    }


    @GetMapping("/geoIP/{ipAddress}")
    public GeoIP getLocation(@PathVariable String ipAddress, HttpServletRequest request
    ) throws IOException, GeoIp2Exception {
        return geoIPLocationService.getIpLocation(ipAddress, request);
    }}
