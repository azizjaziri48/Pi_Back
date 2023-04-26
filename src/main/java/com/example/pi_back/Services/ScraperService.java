package com.example.pi_back.Services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class ScraperService {
    public Set<String> scrapeFacebook(String url ) throws InterruptedException {
        Pattern p = Pattern.compile("20");
        //créer et associer le moteur à la regex sur la chaîne "ab"

        EdgeOptions edgeOptions = new EdgeOptions();

        System.setProperty("webdriver.edge.driver", "C:/Users/Moetez/IdeaProjects/Pi_Back/src/main/resources/msedgedriver.exe");

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().window().maximize();
        driver.get("https://www.facebook.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement email_field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[1]/div[1]/input")));
        email_field.sendKeys("moetez.khemissi@esprit.tn");
        Thread.sleep(1500);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement password_field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[1]/div[2]/div/input")));
        password_field.sendKeys("TestPidev123");
        Thread.sleep(1500);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement confirm_login = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div[1]/div[1]/div/div/div/div[2]/div/div[1]/form/div[2]/button")));
        confirm_login.click();
        Thread.sleep(20000);
        driver.get(url);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement basic_info = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div/div/div[1]/div/div/div/div/div[1]/div[5]/a")));
        Actions action = new Actions(driver);
        Thread.sleep(10000);
        action.moveToElement(basic_info).click().perform();
        basic_info.click();
        action.moveToElement(basic_info).click().perform();
        basic_info.click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement name_container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[1]/div[2]/div/div/div/div[3]/div/div/div/div/div/span/h1")));
        System.out.println("Full name is :" + name_container.getText());
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement Education_button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div/div/div[1]/div/div/div/div/div[1]/div[3]/a")));
        action.moveToElement(Education_button).click().perform();
        System.out.println("Hi i am here");
        Thread.sleep(5000);
        WebElement Education_container = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            Education_container = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/div/div[5]/div/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div/div/div[1]/div/div/div/div/div[2]/div/div")));
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println("Something wen wrong" + e);
        }


        List<WebElement> education_elements = Education_container.findElements(By.tagName("span"));
        Set<String> education_strings = new HashSet<>();
        for (WebElement part : education_elements) {
            Matcher m = p.matcher(part.getText());
            if(!(m.find() || part.getText().contains("promotion") || part.getText().contains("Promotion") || part.getText().equals("Université") || part.getText().equals("Believe, juste believe and never stop believing.")|| part.getText().contains("Aucun lieu de travail à afficher") || part.getText().equals("Collège/lycée"))){education_strings.add(part.getText());}


        }

        driver.quit();
        return education_strings;

    }
    }
