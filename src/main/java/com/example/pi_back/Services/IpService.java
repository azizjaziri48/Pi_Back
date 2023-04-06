package com.example.pi_back.Services;



import com.example.pi_back.Entities.GeoIP;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IpService {
    GeoIP getIpLocation(String ip, HttpServletRequest request) throws IOException, GeoIp2Exception;
}