package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.GeoIP;
import com.example.pi_back.Services.IpService;

import com.example.pi_back.Services.IpServiceImpl;
import com.example.pi_back.Services.QrCodeService;
import com.example.pi_back.Services.ScraperService;
import com.google.zxing.WriterException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@RestController
public class GeoIPController {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    private final QrCodeService qrservice;
    public final ScraperService scraperService;
    private final IpService geoIPLocationService;

    public GeoIPController(QrCodeService qrservice, ScraperService scraperService, IpServiceImpl geoIPLocationService) {
        this.qrservice = qrservice;
        this.scraperService = scraperService;
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
    @GetMapping("/OCR")
    public String OCR () throws TesseractException {

String inputFilePath = "C:/Users/Moetez/IdeaProjects/Pi_Back/uploads/Cap2.png";
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Users/Moetez/IdeaProjects/Pi_Back/src/main/resources/Tesseract");
        String fullText = tesseract.doOCR(new File(inputFilePath));
        System.out.println("this is full text"+fullText);
        String lines[] = fullText.split("\\r?\\n");
        for (String var : lines)
        {
            if (!var.equals("")){
                if( var.contains("firstName")){
                    String[] var2 = var.split(":");
                    System.out.println("First name is"+var2[var2.length-1]);

                }
                else if( var.contains("email")){
                    String[] var2 = var.split(":");
                    System.out.println("email is"+var2[var2.length-1]);
                }
                else if( var.contains("password")){
                    String[] var2 = var.split(":");
                    System.out.println("password is"+var2[var2.length-1]);

                }
                else{
                    String[] var2 = var.split(":");
                    System.out.println("Las name is"+var2[var2.length-1]);

                }
            }


        }
        System.out.println(lines);
        return fullText;
    }
    @GetMapping("/selenium")
    public Set<String> sel (@RequestParam("url") String url) throws InterruptedException {
      return  scraperService.scrapeFacebook(url);
    }
}