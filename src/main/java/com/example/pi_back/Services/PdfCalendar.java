package com.example.pi_back.Services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.time.LocalDate;

public class PdfCalendar extends PdfPageEventHelper {


    private LocalDate startDate;
    private LocalDate endDate;

    public PdfCalendar(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            // Table pour le calendrier
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new int[] {1, 1, 1, 1, 1, 1, 1});
            // Ajouter les en-têtes de colonnes
            String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
            for (String day : days) {
                PdfPCell cell = new PdfPCell(new Phrase(day, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorderColor(BaseColor.GRAY);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
            // Ajouter les cases pour chaque jour du mois
            LocalDate date = startDate;
            while (date.isBefore(endDate.plusDays(1))) {
                // Ajouter une cellule pour le jour de la semaine
                PdfPCell dayCell = new PdfPCell(new Phrase(String.valueOf(date.getDayOfMonth()), FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
                dayCell.setBorderColor(BaseColor.GRAY);
                dayCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                dayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // Colorier la cellule si elle appartient au mois en cours
                if (date.getMonthValue() == startDate.getMonthValue()) {
                    dayCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                }
                table.addCell(dayCell);
                // Passer au jour suivant
                date = date.plusDays(1);
            }
            // Ajouter le calendrier en tant que pied de page
            table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 10, cb);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }}
