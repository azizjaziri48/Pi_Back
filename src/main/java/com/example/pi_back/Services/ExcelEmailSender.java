package com.example.pi_back.Services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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


@Component
public class ExcelEmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithExcelAttachment(String toEmail, Credit credit, User user, List<Amortissement> amortissementList)
            throws IOException, MessagingException {

        // Create Excel Workbook and Sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Amortissement");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Amortissement");
        headerRow.createCell(1).setCellValue("Intérêts");
        headerRow.createCell(2).setCellValue("Mensualité");
        headerRow.createCell(3).setCellValue("Capital restant dû");

        // Create Data Rows
        int rowIndex = 1;
        for (Amortissement amortissement : amortissementList) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(amortissement.getAmortissement());
            row.createCell(1).setCellValue(amortissement.getInterest());
            row.createCell(2).setCellValue(amortissement.getMensualite());
            row.createCell(3).setCellValue(amortissement.getMontantR());
        }

        // Write the Workbook to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] bytes = outputStream.toByteArray();

        // Create a DataSource from the byte array
        DataSource dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");

        // Create a MimeMessage and MimeMessageHelper for sending the email
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(toEmail);
        messageHelper.setSubject("Confirmation de crédit ajouté");
        messageHelper.setText("Bonjour " + user.getFirstname() + ",\n\nVotre crédit de " + credit.getAmount() + " a été ajouté avec succès à la date " + credit.getObtainingDate() + ".\nVoici ci-joint l'attachment Excel.\n\nCordialement,\nVotre équipe de microfinance");

        // Create a MimeMultipart for adding the Excel attachment
        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
        attachmentBodyPart.setFileName("Amortissement.xlsx");
        multipart.addBodyPart(attachmentBodyPart);

        // Set the MimeMultipart as the content of the MimeMessage
        messageHelper.getMimeMessage().setContent(multipart);

        // Send the email
        mailSender.send(mimeMessage);

        // Close the Workbook and OutputStream
        workbook.close();
        outputStream.close();
    }
}
