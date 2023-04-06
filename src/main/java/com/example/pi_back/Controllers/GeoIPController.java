package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.GeoIP;
import com.example.pi_back.Services.IpService;

import com.example.pi_back.Services.IpServiceImpl;
import com.example.pi_back.Services.QrCodeService;
import com.google.zxing.WriterException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.WebDriver;

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
    @GetMapping("/OCR")
    public String OCR () throws TesseractException {
String inputFilePath = "C:/Users/Moetez/IdeaProjects/Pi_Back/uploads/nohand.jpg";
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Users/Moetez/IdeaProjects/Pi_Back/src/main/resources/Tesseract");
        String fullText = tesseract.doOCR(new File(inputFilePath));
        System.out.println("this is full text"+fullText);
        return fullText;
    }
    @GetMapping("/selenium")
    public String sel () throws InterruptedException {

        EdgeOptions edgeOptions = new EdgeOptions();

System.setProperty("webdriver.edge.driver","C:/Users/Moetez/IdeaProjects/Pi_Back/src/main/resources/msedgedriver.exe");

                WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().window().maximize();
                driver.get("https://www.facebook.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement email_field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[1]/div[1]/input")));
        email_field.sendKeys("moetez.khemissi@esprit.tn");
        Thread.sleep(500);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement password_field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[1]/div[2]/div/input")));
        password_field.sendKeys("TestPidev123");
        Thread.sleep(500);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirm_login = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[2]/button")));
        confirm_login.click();
        Thread.sleep(5000);
        driver.get("https://www.facebook.com/rayanrejeb/about");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement basic_info = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div/div/div[1]/div/div/div/div/div[1]/div[5]/a")));
        Actions action = new Actions(driver);

        action.moveToElement(basic_info).click().perform();
        basic_info.click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement name_container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[1]/div[2]/div/div/div/div[3]/div/div/div/div/div/span/h1")));
        System.out.println("Full name is :"+name_container.getText());
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement Education_button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div/div/div[1]/div/div/div/div/div[1]/div[3]/a")));
        action.moveToElement(Education_button).click().perform();
        action.moveToElement(Education_button).click().perform();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(5000);
        WebElement Education_container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div/div/div[1]/div/div/div/div/div[2]/div/div")));
        Thread.sleep(2000);
        List<WebElement> education_elements = Education_container.findElements(By.tagName("span"));
        System.out.println(education_elements);
        Set<String> education_strings = new HashSet<>();
        for (WebElement part : education_elements) {
            education_strings.add(part.getText());

        }
        System.out.println(education_strings);
        /*TODO scrape where study*/
        Thread.sleep(155000);
                driver.quit();
            return "Done Scraping";

    }

}