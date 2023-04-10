package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CalendarGeneratorService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generateCalendar(List<Project> projects) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
        PdfWriter.getInstance(document, baos);

        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Calendrier des projets", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 4, 4});

        addTableHeader(table);

        for (Project project : projects) {
            addTableData(table, project);
        }

        document.add(table);

        document.close();

        return baos.toByteArray();
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        PdfPCell cell = new PdfPCell(new Paragraph("Nom du projet", headerFont));
        cell.setBackgroundColor(new BaseColor(12, 97, 147));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Date de début", headerFont));
        cell.setBackgroundColor(new BaseColor(12, 97, 147));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Date de fin", headerFont));
        cell.setBackgroundColor(new BaseColor(12, 97, 147));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTableData(PdfPTable table, Project project) {
        PdfPCell cell = new PdfPCell(new Paragraph(project.getName()));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(project.getStartdate().format(FORMATTER)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph(project.getEnddate().format(FORMATTER)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
