package com.example.pi_back.Services;

import com.example.pi_back.Entities.Project;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

public class ProjectPdfGenerator {

    private List<Project> projects;

    public ProjectPdfGenerator(List<Project> projects) {
        this.projects = projects;
    }

    public void generatePdf(String filePath) throws Exception {
        // Créer le document PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Couleur de fond
        document.add(new Rectangle(PageSize.A4));
        document.add(new Paragraph(" ")); // Ajouter une ligne vide pour l'espacement
        // Ajouter un titre
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.YELLOW.darker());
        Paragraph title = new Paragraph("Liste des projets", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);


        // Ajouter le tableau des projets
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 4, 4, 4, 4, 4, 4,4 });

        addTableHeader(table);
        addTableData(table);

        document.add(table);

        // Ajouter la statistique
        Font statFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph stat = new Paragraph(getStatistique(), statFont);
        stat.setAlignment(Element.ALIGN_CENTER);
        stat.setSpacingBefore(20f);
        document.add(stat);
        Paragraph avgStat = new Paragraph(getAverageInvestment(), statFont);
        avgStat.setAlignment(Element.ALIGN_CENTER);
        avgStat.setSpacingBefore(20f);
        document.add(avgStat);
// Ajouter le graphique de la répartition par catégorie
        JFreeChart chart = createChart();
        int width = 500;
        int height = 300;
        String chartPath = "chart.png";
        ChartUtilities.saveChartAsPNG(new java.io.File(chartPath), chart, width, height);
        Image chartImage = Image.getInstance(chartPath);
        chartImage.scaleToFit(500f, 250f);
        document.add(chartImage);
        addCalendarTable(document);


        // Fermer le document PDF
        document.close();
    }

    private String getAverageInvestment() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Project project : projects) {
            totalAmount = totalAmount.add(project.getAmountinvestment());
        }
        BigDecimal average = totalAmount.divide(BigDecimal.valueOf(projects.size()), 2, RoundingMode.HALF_UP);
        return "Moyenne d'investissement par projet : " + average;
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(12, 97, 147));
        cell.setPadding(5);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        cell.setPhrase(new Phrase("Nom", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Catégorie", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Town", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Montant", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Final Amount", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Devise", headerFont));
        table.addCell(cell);




    }

    private void addTableData(PdfPTable table) {
        for (Project project : projects) {
            table.addCell(project.getName());
            table.addCell(project.getDescription());
            table.addCell(project.getCategory());
            table.addCell(project.getCountry());
            table.addCell(String.valueOf(project.getAmountinvestment()));
            table.addCell(String.valueOf(project.getFinalamount()));
            table.addCell(project.getCurrency().toString());
        }
    }

    private String getStatistique() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalInvestors = 0;
        for (Project project : projects) {
            totalAmount = totalAmount.add(project.getAmountinvestment());
        }

        return "Total montant investi : " + totalAmount + "\n"
                + "Nombre total d'investisseurs : " + totalInvestors;
    }


    private JFreeChart createChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Project project : projects) {
            dataset.setValue(project.getCategory(), project.getAmountinvestment());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Répartition des amounts par catégorie",  // chart title
                dataset,                      // data
                true,                         // include legend
                true,                         // tooltips
                false                         // URLs
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.GRAY);
        chart.setBorderStroke(new BasicStroke(2));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setNoDataMessage("Aucune donnée disponible");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        plot.setIgnoreNullValues(true);
        plot.setIgnoreZeroValues(true);
        plot.setBackgroundPaint(null);
        plot.setOutlinePaint(null);
        plot.setShadowPaint(null);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
        plot.setLegendLabelToolTipGenerator(new StandardPieSectionLabelGenerator("{0} - {1}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
        plot.setSimpleLabels(true);
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 128));
        plot.setLabelShadowPaint(new Color(0, 0, 0, 64));
        plot.setLabelOutlinePaint(null);
        plot.setLabelLinkStroke(new BasicStroke(1.0f));
        plot.setLabelLinkPaint(new Color(0, 0, 0, 64));
        plot.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);
        plot.setLabelPaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }
    private void addCalendarTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 3, 3, 3,3 });
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

        // Ajouter l'en-tête de la table
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(12, 97, 147));
        cell.setPadding(5);
        cell.setPhrase(new Phrase("Projet", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Start Date", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("End-Date", headerFont));
        table.addCell(cell);

        // Ajouter les données
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (Project project : projects) {
            table.addCell(new Phrase(project.getName(), dataFont));
            table.addCell(new Phrase(project.getCategory(), dataFont));
            table.addCell(new Phrase(String.valueOf(project.getStartdate()), dataFont));
            table.addCell(new Phrase(String.valueOf(project.getEnddate()), dataFont));

        }

        // Ajouter la table au document
        document.add(table);
    }
    // Créer le calendrier



}