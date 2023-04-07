package com.example.pi_back.Services;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.DuesHistory;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.DuesHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.example.pi_back.Repositories.DuesHistoryRepository;
import org.springframework.core.io.FileSystemResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    DuesHistoryRepository DHrepo;
    @Autowired
    CreditManipulationService creditservice;

    public void sendConfirmationEmail(String recipient, Credit credit, User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipient);
        mail.setSubject("Confirmation de crédit ajouté");
        mail.setText("Bonjour " + user.getFirstname() + ",\n\nVotre crédit de " + credit.getAmount() + " a été ajouté avec succès à la date " + credit.getObtainingDate() + ".\n\nCordialement,\nVotre équipe de microfinance");
        javaMailSender.send(mail);
    }

    public void sendEmailWithExcelAttachment(String recipient, Credit credit, User user, List<Amortissement> amortissementList)
            throws IOException, MessagingException {

        // Create Excel Workbook and Sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Amortissement");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Echéance N°");
        headerRow.createCell(1).setCellValue("Montant restant dù");
        headerRow.createCell(2).setCellValue("Interet");
        headerRow.createCell(3).setCellValue("Amortissement ");
        headerRow.createCell(4).setCellValue("Mensualité");

        // Create Data Rows
        int rowIndex = 1;
        for (Amortissement amortissement : amortissementList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(rowIndex - 2);
            row.createCell(1).setCellValue(amortissement.getMontantR());
            row.createCell(2).setCellValue(amortissement.getInterest());
            row.createCell(3).setCellValue(amortissement.getAmortissement());
            row.createCell(4).setCellValue(amortissement.getMensualite());
        }

        // Write the Excel file to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        // Create the email message with the Excel file as an attachment
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient);
        helper.setSubject("Confirmation de crédit ajouté");
        helper.setText("Bonjour " + user.getFirstname() + ",\n\nVotre crédit de " + credit.getAmount() + " a été ajouté avec succès à la date " + credit.getObtainingDate() + ".\n\nCordialement,\nVotre équipe de microfinance");
        helper.addAttachment("Amortissement.xlsx", new ByteArrayResource(outputStream.toByteArray()));

        // Send the email
        javaMailSender.send(message);

        // Close the streams
        outputStream.close();
        workbook.close();
    }

    public void sendFinishEmail(String recipient, DuesHistory DH) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipient);
        mail.setSubject("Crédit remboursé");
        mail.setText("Bonjour " + DH.getCredits().getUser().getFirstname() + ",\n\nVotre crédit de " + DH.getCredits().getAmount() + " a été remboursé avec succès le " + DH.getSupposedDate() + ".\n\nCordialement,\nVotre équipe de microfinance");
        javaMailSender.send(mail);
    }

    public void sendRefusEmail(String recipient, User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipient);
        mail.setSubject("Crédit refusé");
        mail.setText("Bonjour " + user.getFirstname() + ",\n\nNous regrettons de vous informer que vous êtes désormais interdit de crédit. \nN'hésitez pas à nous contacter si vous avez des questions ou si vous souhaitez discuter de votre situation avec notre équipe. \n\nCordialement,\nVotre équipe de microfinance");
        javaMailSender.send(mail);
    }

    public void sendGarantEmail(String recipient, Credit credit, User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipient);
        mail.setSubject("Crédit refusé");
        mail.setText("Bonjour " + user.getFirstname() + ",\n\nNous regrettons de vous informer que votre demande de crédit " + credit.getAmount() + " que vous avez demandé le " + credit.getDateDemande() + " n'a pas été traitée avec succès et que nous ne pouvons pas ajouter votre crédit à notre système pour le moment. \nSelon nos critères d'approbation de crédit, le salaire garant doit être égal ou supérieur à 0,33 fois le montant du crédit demandé. \nN'hésitez pas à nous contacter si vous avez des questions ou si vous souhaitez discuter de votre situation avec notre équipe. \n\nCordialement,\nVotre équipe de microfinance");
        javaMailSender.send(mail);
    }
}
