package com.example.pi_back.Controllers;

import com.example.pi_back.Services.PowerBiAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessTokenController {

    @GetMapping("/token")
    public String getToken() throws Exception {
        return PowerBiAuthentication.getAccessToken();
    }
}
