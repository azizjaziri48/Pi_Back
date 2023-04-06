package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.GeoIP;
import com.example.pi_back.Services.IpService;

import com.example.pi_back.Services.IpServiceImpl;
import com.example.pi_back.Services.QrCodeService;
import com.google.zxing.WriterException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class GeoIPController {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    private final QrCodeService qrservice;
    private final IpService geoIPLocationService;

    public GeoIPController(QrCodeService qrservice, IpServiceImpl geoIPLocationService) {
        this.qrservice = qrservice;
        this.geoIPLocationService = geoIPLocationService;
    }

    @GetMapping("/geoIP/{ipAddress}")
    public GeoIP getLocation(@PathVariable String ipAddress, HttpServletRequest request
    ) throws IOException, GeoIp2Exception {
        return geoIPLocationService.getIpLocation(ipAddress, request);
    }
    @GetMapping(value = "/QR")
public String generate_qr() throws IOException, WriterException {
    qrservice.generateQRCodeImage(500,500);
    return "done";
}

    @GetMapping(
            value = "/get-image-with-media-type",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/static/img/QRCode.png");
        return IOUtils.toByteArray(in);
    }
    @PostMapping  ("/photos/add")
    public String addPhoto(
                           @RequestParam("image") MultipartFile image) throws IOException { StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename());
        fileNames.append(image.getOriginalFilename());
        Files.write(fileNameAndPath, image.getBytes());
        return "done";
    }

}