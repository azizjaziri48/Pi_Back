package com.example.pi_back.Services;


import com.example.pi_back.Entities.GeoIP;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Objects.nonNull;
@Service
public class IpServiceImpl implements IpService {

    private final DatabaseReader databaseReader;
    private static final String UNKNOWN = "UNKNOWN";
    public String ip="";

    public String GetIP() throws MalformedURLException {
        String urlString = "http://checkip.amazonaws.com/";
        URL url = new URL(urlString);
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            ip= br.readLine();
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }


    public IpServiceImpl(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    /**
     * Get device info by user agent
     *
     * @param userAgent user agent http device
     * @return Device info details
     * @throws IOException if not found
     */
    private String getDeviceDetails(String userAgent) throws IOException {
        String deviceDetails = UNKNOWN;

        Parser parser = new Parser();

        Client client = parser.parse(userAgent);
        if (nonNull(client)) {
            deviceDetails = client.userAgent.family + " " + client.userAgent.major + "." + client.userAgent.minor +
                    " - " + client.os.family + " " + client.os.major + "." + client.os.minor;
        }

        return deviceDetails;
    }

    /**
     * get user position by ip address
     *
     * @param ip String ip address
     * @return UserPositionDTO model
     * @throws IOException     if local database city not exist
     * @throws GeoIp2Exception if cannot get info by ip address
     */

    public GeoIP getIpLocation(String ip, HttpServletRequest request) throws IOException, GeoIp2Exception {

        GeoIP position = new GeoIP();
        String location;

        InetAddress ipAddress = InetAddress.getByName(ip);

        CityResponse cityResponse = databaseReader.city(ipAddress);
        if (nonNull(cityResponse) && nonNull(cityResponse.getCity())) {

            String continent = (cityResponse.getContinent() != null) ? cityResponse.getContinent().getName() : "";
            String country = (cityResponse.getCountry() != null) ? cityResponse.getCountry().getName() : "";

            location = String.format("%s, %s, %s", continent, country, cityResponse.getCity().getName());
            position.setCity(cityResponse.getCity().getName());
            position.setFullLocation(location);
            position.setLatitude((cityResponse.getLocation() != null) ? cityResponse.getLocation().getLatitude() : 0);
            position.setLongitude((cityResponse.getLocation() != null) ? cityResponse.getLocation().getLongitude() : 0);
            position.setDevice(getDeviceDetails(request.getHeader("user-agent")));
            position.setIpAddress(ip);

        }
        return position;
    }
}

