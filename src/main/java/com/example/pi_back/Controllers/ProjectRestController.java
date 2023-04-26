package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Project;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.ProjectRepository;
import com.example.pi_back.Services.ProjectService;
import com.example.pi_back.Services.UserService;
import com.example.pi_back.config.MyResourceNotFoundException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Cell;
import com.lowagie.text.StandardFonts;
import com.lowagie.text.Table;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.opencsv.CSVWriter;
import com.sun.istack.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import net.glxn.qrgen.QRCode;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Text;


@RestController
@AllArgsConstructor
@RequestMapping("/Project")
public class ProjectRestController {
    private ProjectService ProjectService;
    private final ProjectRepository projectRepository;
    private UserService userService;

    //Affichage de tous les projets
    @GetMapping("/retrieve-all-investesments")
    @ResponseBody
    public List<Project> getProject() {
        List<Project> listProj = ProjectService.retrieveAllProject();
        return listProj;
    }

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ProjectService projectService;


    @GetMapping("/export/cs")
    public ResponseEntity<byte[]> exportCsv() throws IOException {
        List<Project> projects = ProjectService.getAlProject();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));
        csvWriter.writeNext(new String[]{"Id", "Name", "Description", "Amount Investment", "Number of Investors", "Investment Rate", "Intev Age", "Risk Score", "Country", "Start Date", "End Date", "Category", "Final Amount"});
        for (Project project : projects) {
            csvWriter.writeNext(new String[]{
                    String.valueOf(project.getId()),
                    project.getName(),
                    project.getDescription(),
                    String.valueOf(project.getAmountinvestment()),
                    String.valueOf(project.getTauxinvest()),
                    project.getIntevage().toString(),
                    String.valueOf(project.getRiskscore()),
                    project.getCountry(),
                    project.getStartdate().format(DateTimeFormatter.ISO_DATE),
                    project.getEnddate().format(DateTimeFormatter.ISO_DATE),
                    project.getCategory(),
                    String.valueOf(project.getFinalamount())
            });
        }
        csvWriter.flush();
        csvWriter.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "projects.csv");
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
    /*@PostMapping("/add_PCinvestesment/{Fund-id}")
    public Project addProject(@RequestBody Project project, @PathVariable("Fund-id") Long idFund) throws MessagingException, IOException {

        Project p = projectService.addProject(project, idFund);

        projectService.finalAmount();

        // Lecture de l'image d'arrière-plan à partir du dossier des ressources
        //BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/p.png"));
        // Obtenir la largeur de l'image en pixels
        //  int width = backgroundImage.getWidth();
        //  System.out.println(width);
// Création du contenu HTML avec l'image en arrière-plan et les informations supplémentaires
        String htmlContent = "<html><body style='margin:0; padding:0;'>" +
                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "<td></td>" +
                "</tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "<td></td>" +
                "</tr>" +
                "<td background='cid:backgroundImage' bgcolor='#ffffff' valign='top' style='background-position: center top; background-size:cover; background-repeat: no-repeat; padding: 50px 15px;'>" +
                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<td align='center' valign='bottom'>" +
                "<h1 style='color:#FAEE4F;text-shadow: 1px 1px #000000;font-size:30;font-weight:bold;'>There's a new Project</h1>" +
                "  <p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:20; font-weight:bold;'>" + p.getDescription() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Amount of investment: </strong> " + p.getAmountinvestment() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>If you want to contact us :</strong> </p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Phone number :</strong> +216 55 555 555 : </p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Mail :</strong> MIDAS@gmail.com: </p>" +


                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<td align='center' style='padding: 20px 0;'>" +
                "<table border='0' cellpadding='0' cellspacing='0'>" +
                "<tr>" +
                "<td align='center' bgcolor='#4169E1' style='border-radius: 3px;'>" +
                "<a href='https://votre_site_web.com' target='_blank' style='font-size: 16px; font-family: Arial, sans-serif; color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 3px; display: block;'>Voir le projet</a>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "<td></td>" +
                "</tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "</tr>" +

                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</body></html>";

// Création du message avec contenu HTML et image en pièce jointe
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject("New Project!");
        helper.setFrom("chouchanecyrine@gmail.com");
        helper.setTo("cyrine.chouchane@esprit.tn");
        helper.setText(htmlContent, true);

// Ajout de l'image en pièce jointe
        BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/try.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(backgroundImage, "png", baos);
        ByteArrayDataSource backgroundDataSource = new ByteArrayDataSource(baos.toByteArray(), "image/png");
        helper.addInline("backgroundImage", backgroundDataSource);

// Envoi du message
        javaMailSender.send(message);

        return p;
    }*/

   /* @PostMapping("/add_PCinvestesment/{Fund-id}")
    public Project addProject(@RequestBody Project project, @PathVariable("Fund-id") Long idFund) throws MessagingException, IOException {
        // Calcul du score de risque en fonction du montant d'investissement
        double amount = project.getAmountinvestment();
        int riskscore = 0;
        if (amount < 5000) {
            riskscore = 1;
        } else if (amount >= 5000 && amount < 10000) {
            riskscore = 2;
        } else if (amount >= 10000 && amount < 50000) {
            riskscore = 3;
        } else if (amount >= 50000 && amount < 100000) {
            riskscore = 4;
        } else if (amount >= 100000) {
            riskscore = 5;
        }
        project.setRiskscore(riskscore);

        // Calcul du montant final
        double finalamount = amount * (1 + riskscore * 0.1);
        project.setFinalamount((float) finalamount);

        // Ajouter le projet à la base de données
        Project p = projectService.addProject(project, idFund);
        projectService.finalAmount();

        // Lecture de l'image d'arrière-plan à partir du dossier des ressources
        //BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/p.png"));
        // Obtenir la largeur de l'image en pixels
        //  int width = backgroundImage.getWidth();
        //  System.out.println(width);
// Création du contenu HTML avec l'image en arrière-plan et les informations supplémentaires
        String htmlContent = "<html><body style='margin:0; padding:0;'>" +
                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "<td></td>" +
                "</tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "<td></td>" +
                "</tr>" +
                "<td background='cid:backgroundImage' bgcolor='#ffffff' valign='top' style='background-position: center top; background-size:cover; background-repeat: no-repeat; padding: 50px 15px;'>" +
                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<td align='center' valign='bottom'>" +
                "<h1 style='color:#FAEE4F;text-shadow: 1px 1px #000000;font-size:30;font-weight:bold;'>There's a new Project</h1>" +
                "  <p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:25; font-weight:bold;'>" + p.getDescription() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Category: </strong> " + p.getCategory() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Start date: </strong> " + p.getStartdate() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>End Date: </strong> " + p.getEnddate() + "</p>" +


                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Amount of investment: </strong> " + p.getAmountinvestment() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Score de risque: </strong> " + p.getRiskscore() + "</p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Final Amount: </strong> " + p.getFinalamount() + "</p>" +

                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>If you want to contact us :</strong> </p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Phone number :</strong> +216 55 555 555 : </p>" +
                "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Mail :</strong> MIDAS@gmail.com: </p>" +


                "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                "<tr>" +
                "<td align='center' style='padding: 20px 0;'>" +
                "<table border='0' cellpadding='0' cellspacing='0'>" +
                "<tr>" +
                "<td align='center' bgcolor='#4169E1' style='border-radius: 3px;'>" +
                "<a href='https://votre_site_web.com' target='_blank' style='font-size: 16px; font-family: Arial, sans-serif; color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 3px; display: block;'>Voir le projet</a>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</td>" +
                "</tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "<td></td>" +
                "</tr>" +
                "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                "</tr>" +

                "</table>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</body></html>";

// Création du message avec contenu HTML et image en pièce jointe
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject("New Project!");
        helper.setFrom("chouchanecyrine@gmail.com");
        helper.setTo("cyrine.chouchane@esprit.tn");
        helper.setText(htmlContent, true);

// Ajout de l'image en pièce jointe
        BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/try.png"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(backgroundImage, "png", baos);
        ByteArrayDataSource backgroundDataSource = new ByteArrayDataSource(baos.toByteArray(), "image/png");
        helper.addInline("backgroundImage", backgroundDataSource);

// Envoi du message
        javaMailSender.send(message);

        return p;
    }*/
    // Tableau et graphique pour afficher les informations sur les projets

    @GetMapping("/getTabInvest")
    public ResponseEntity<String> getAllTProjects() {
        List<Project> projects = ProjectService.retrieveAllProject();

        // Générer le tableau HTML
        String table = "<table><tr><th>Project ID</th><th>Project Name</th><th>Investment Amount</th></tr>";
        for (Project project : projects) {
            table += "<tr><td>" + project.getId() + "</td><td>" + project.getName() + "</td><td>" + project.getAmountinvestment() + "</td></tr>";
        }
        table += "</table>";

        // Générer le graphique
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Project project : projects) {
            dataset.addValue(project.getAmountinvestment(), "Investment Amount", project.getName());
        }
        JFreeChart chart = ChartFactory.createBarChart("Investment Amounts by Project", "Project Name", "Investment Amount", dataset, PlotOrientation.VERTICAL, false, true, false);
        BufferedImage image = chart.createBufferedImage(600, 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(bytes);
        String chartImg = "<img src='data:image/png;base64," + base64Image + "'/>";

        // Combiner le tableau HTML et le graphique
        String report = "<h2>Investment Amounts by Project</h2>" + table + chartImg;

        return new ResponseEntity<>(report, HttpStatus.OK);
    }



    @GetMapping("/retrievee-all-investments")
    @ResponseBody
    public List<Project> gettAlllProjects() {
        List<Project> projects = ProjectService.retrieveAlllProject();
        for (Project project : projects) {
            // Récupérer le nombre de "j'aime" et de "je n'aime pas" pour le projet
            int numLikes = project.getLikes().size();
            int numDislikes = project.getDislikes().size();
            // Ajouter les nombres de "j'aime" et "je n'aime pas" à la description du projet
            project.setDescription(project.getDescription() + "<br>" + numLikes + " j'aime, " + numDislikes + " je n'aime pas");
        }
        return projects;
    }

    @GetMapping("/retrieve-projects-by-fund/{Fund-id}")
    @ResponseBody
    public List<Project> getProjectbyFund(@PathVariable("Fund-id") Long idFund) {
        List<Project> listProj = projectService.retrieveProjectbyFund(idFund);
        return listProj;
    }

    //Affichage de tous les projets
 /*   @GetMapping("/retrieve-all-projects")
    public List<Project> getProject() {
        return ProjectService.retrieveAllProject();
    }*/


    //suppression de tous les projets



    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> removeProject(@PathVariable("id") Integer idProject) {
        if (ProjectService.retrieveProject(idProject) == null) {
            return new ResponseEntity<>("The Project to be deleted doesn't exist", HttpStatus.NOT_FOUND);
        }


        ProjectService.removeProject(idProject);
        return new ResponseEntity<>("Project was deleted successfully", HttpStatus.OK);
    }

    //Affichage d'un seul projet

    @GetMapping("/get/{id}")
    ResponseEntity<Project> retrieveProject(@PathVariable("id") Integer idProject) {
        Project Retrieved_Project = ProjectService.retrieveProject(idProject);
        if (Retrieved_Project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Retrieved_Project, HttpStatus.OK);

    }

    //Mise à jour d'un projet
    @PutMapping("/update")
    ResponseEntity<String> updateProject(@RequestBody Project Project) {
        if (ProjectService.retrieveProject(Project.getId()) == null) {
            return new ResponseEntity<>("Project Doesn't exist", HttpStatus.BAD_REQUEST);
        }
        ProjectService.updateProject(Project);
        return new ResponseEntity<>("Project updated successfully", HttpStatus.OK);
    }


    //Calcul du montant d'investissement selon un projet
    @GetMapping("/CalculateAmountOfInves/{Project-id}")
    @ResponseBody
    public void CalculateAmoutOfInves(@PathVariable("Project-id") int id) {
        ProjectService.CalculateAmountOfInves(id);
    }

    @GetMapping("/finalAmount")
    @ResponseBody
    public void finalAmount() {
        ProjectService.finalAmount();
    }

    @GetMapping("/CalculateRateOfInves/{Project-id}")
    @ResponseBody
    public float CalculateRateOfInves(@PathVariable("Project-id") int idProject) {
        return ProjectService.calculateRateOfInves(idProject);
    }

    @GetMapping("/Rate/{amountinvestment}")
    @ResponseBody
    public double Rate(@PathVariable("amountinvestment") float AmountInvestestesment) {
        return ProjectService.Rate(AmountInvestestesment);
    }

    @GetMapping("/retrieve-all-likes")
    @ResponseBody
    public List<Project> getAlllProjects() {
        List<Project> projects = projectService.retrieveAllProject();
        for (Project project : projects) {
            // Récupérer le nombre de "j'aime" et de "je n'aime pas" pour le projet
            int numLikes = project.getLikes().size();
            int numDislikes = project.getDislikes().size();
            // Ajouter les nombres de "j'aime" et "je n'aime pas" à la description du projet
            project.setDescription(project.getDescription() + "<br>" + numLikes + " j'aime, " + numDislikes + " je n'aime pas");
        }
        return projects;
    }

    //Envoi pdf
    @GetMapping("/send-project-report/{project-id}")
    public ResponseEntity<String> sendProjectReport(@PathVariable("project-id") Integer projectId) {
        // Récupérer le projet spécifié
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new MyResourceNotFoundException("Project not found"));

        // Générer le contenu du PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Projet " + project.getName()));
            document.add(new Paragraph("Description: " + project.getDescription()));
            document.add(new Paragraph("Catégorie: " + project.getCategory()));
            document.add(new Paragraph("Montant: " + project.getAmountinvestment()));
            document.add(new Paragraph("Taux d'investissement: " + project.getTauxinvest()));
            document.add(new Paragraph("MontantFinal: " + project.getFinalamount()));
            document.add(new Paragraph("Age: " + project.getIntevage()));
            document.add(new Paragraph("Country: " + project.getCountry()));


            document.close();
        } catch (com.itextpdf.text.DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while generating PDF");
        }

        // Envoyer le PDF par e-mail
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Rapport de projet " + project.getName());
            helper.setFrom("chouchanecyrine@gmail.com");
            helper.setTo("cyrine.chouchane@esprit.tn");
            helper.setText("Veuillez trouver ci-joint le rapport de projet " + project.getName() + ".");
            helper.addAttachment("project_report.pdf", new ByteArrayResource(outputStream.toByteArray()), "application/pdf");
            javaMailSender.send(message);
            return ResponseEntity.ok("Project report sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while sending e-mail");
        }
    }


//conversion normal aadeya

 /*   @GetMapping("/convert/{amount}/{fromCurrency}/{toCurrency}")
    public String convertCurrency(@PathVariable double amount, @PathVariable String fromCurrency, @PathVariable String toCurrency) {
        String apiKey = "f2ba7e8b2b225aff3756ea1a"; // Remplacez par votre propre API key
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return result;
    }
*/


    @GetMapping("/dashboard")
    public ResponseEntity<List<Project>> getProjects(@RequestParam(required = false) String category,
                                                     @RequestParam(required = false) Double amountinvestment) {
        List<Project> projects = projectService.retrieveAllProject();
        if (category != null) {
            projects = projects.stream()
                    .filter(project -> project.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
        if (amountinvestment != null) {
            projects = projects.stream()
                    .filter(project -> project.getAmountinvestment() == amountinvestment)
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @ExceptionHandler(MyResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(MyResourceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/convert-currency")
    @ResponseBody
    public String convertCurrency(Model model) {
        List<Project> projects = projectService.retrieveAllProject();
        String apiKey = "f2ba7e8b2b225aff3756ea1a"; // votre clé API ici

        for (Project project : projects) {
            float amountInvestment = project.getAmountinvestment();
            float finalAmount = project.getFinalamount();
            String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "TND", "USD", apiKey);
            String convertedFinalAmount = CurrencyConverter.convert(finalAmount, "USD", "TND", apiKey);
            project.setAmountinvestment(Float.parseFloat(convertedAmountInvestment));
            project.setFinalamount(Float.parseFloat(convertedFinalAmount));
            projectService.updateProject(project);
        }

        return "La conversion de devises a été effectuée avec succès pour tous les projets.";
    }

    @GetMapping("/convert-currency/{projectId}")
    @ResponseBody
    public String convertCurrency(@PathVariable int projectId) {
        Project project = projectService.retrieveProjectById(projectId);
        String apiKey = "f2ba7e8b2b225aff3756ea1a"; // votre clé API ici

        float amountInvestment = project.getAmountinvestment();
        float finalAmount = project.getFinalamount();
        String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "TND", "USD", apiKey);
        String convertedFinalAmount = CurrencyConverter.convert(finalAmount, "USD", "TND", apiKey);

        // Remplacer le point par une virgule et formater le résultat avec 4 décimales
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRENCH);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,##0.0000", symbols);
        convertedAmountInvestment = df.format(Float.parseFloat(convertedAmountInvestment));
        convertedFinalAmount = df.format(Float.parseFloat(convertedFinalAmount));

        return "Conversion de la devise pour le projet " + project.getName() + ": " + "amountinvestment = " + convertedAmountInvestment + ", finalamount = " + convertedFinalAmount;
    }




    //conversion behya sans qr
 /*  @GetMapping("/retrieve-all-investesments")
   @ResponseBody
   public List<Project> getAllProjects(Model model) {
       List<Project> listProj = projectService.retrieveAllProject();
       listProj.forEach(project -> {
           double amountInvestment = project.getAmountinvestment();
           double finalAmount = project.getFinalamount();
           String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "USD", "TND", "f2ba7e8b2b225aff3756ea1a");
           String convertedFinalAmount = CurrencyConverter.convert(finalAmount, "USD", "TND", "f2ba7e8b2b225aff3756ea1a");
           project.setAmountinvestment(Float.parseFloat(convertedAmountInvestment));
           project.setFinalamount(Float.parseFloat(convertedFinalAmount));
       });
       model.addAttribute("listProj", listProj);
       return listProj;
   }*/


//jasper report

    @GetMapping("/generate-report")
    public void generateReport(HttpServletResponse response) throws JRException, IOException {

        // Chargement du modèle de rapport JasperReport (.jrxml)
        JasperDesign jasperDesign = JRXmlLoader.load(new ClassPathResource("/reports/project-report.jrxml").getInputStream());

        // Compilation du rapport JasperReport (.jasper)
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // Récupération de la liste des projets depuis la base de données
        List<Project> projects = projectRepository.findAll();

        // Création d'une collection de données à partir de la liste des projets
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(projects);

        // Création d'une carte de paramètres pour le rapport JasperReport
        Map<String, Object> parameters = new HashMap<>();

        // Remplissage du rapport JasperReport avec les données et les paramètres
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Exportation du rapport JasperReport au format PDF
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfStream);

        // Envoi du rapport JasperReport au client sous forme de réponse HTTP
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
        response.setContentLength(pdfStream.size());
        ServletOutputStream outputStream = response.getOutputStream();
        pdfStream.writeTo(outputStream);
        outputStream.flush();
        pdfStream.close();
    }


//hedha menghir tri ou recherche
 /*   @GetMapping("/retrieve-all-investesments")
    @ResponseBody
    public List<Project> getAllProjects(Model model) {
        List<Project> listProj = projectService.retrieveAllProject();
        listProj.forEach(project -> {
            double amountInvestment = project.getAmountinvestment();
            double finalAmount = project.getFinalamount();
            String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "TND", "TND", "f2ba7e8b2b225aff3756ea1a");
            String convertedFinalAmount = CurrencyConverter.convert(finalAmount, "TND", "TND", "f2ba7e8b2b225aff3756ea1a");
            project.setAmountinvestment(Float.parseFloat(convertedAmountInvestment));
            project.setFinalamount(Float.parseFloat(convertedFinalAmount));
            String qrCodeData = "Project Name: " + project.getName() + "\n" +
                    "Investment: " + convertedAmountInvestment + "\n" +
                    "Final Amount: " + convertedFinalAmount;
            String base64QRCodeImage = generateBase64QRCodeImage(qrCodeData);
            project.setQrCode(base64QRCodeImage);
        });
        model.addAttribute("listProj", listProj);
        return listProj;
    }

    private String generateBase64QRCodeImage(String qrCodeData) {
        ByteArrayOutputStream stream = QRCode.from(qrCodeData).withSize(250, 250).stream();
        byte[] qrCodeImageBytes = stream.toByteArray();
        return Base64.getEncoder().encodeToString(qrCodeImageBytes);
    }*/

    //affichage tri/recherche/conversion/qr code
    @GetMapping("/retrieve-all-investments")
    @ResponseBody
    public List<Project> getAllProjects(
            Model model,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
    ) {
        List<Project> listProj = projectService.retrieveAllProject();


// Filtrage par recherche
        if (search != null && !search.isEmpty()) {
            listProj = listProj.stream()
                    .filter(project ->
                            project.getName().toLowerCase().contains(search.toLowerCase()) ||
                                    project.getDescription().toLowerCase().contains(search.toLowerCase())
                    )
                    .collect(Collectors.toList());
        }

// Tri des résultats
        if (sortBy != null && !sortBy.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            Comparator<Project> comparator = null;
            switch (sortBy) {
                case "name":
                    comparator = Comparator.comparing(Project::getName);
                    break;
                case "amountinvestment":
                    comparator = Comparator.comparing(Project::getAmountinvestment);
                    break;
                case "finalamount":
                    comparator = Comparator.comparing(Project::getFinalamount);
                    break;
                default:
                    // Retourner la liste sans tri
                    break;
            }

            if (comparator != null) {
                if (sortOrder.equalsIgnoreCase("desc")) {
                    listProj.sort(comparator.reversed());
                } else {
                    listProj.sort(comparator);
                }
            }
        }

// Conversion des montants
        listProj.forEach(project -> {
            double amountInvestment = project.getAmountinvestment();
            double finalAmount = project.getFinalamount();
            String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "TND", "TND", "f2ba7e8b2b225aff3756ea1a");
            String convertedFinalAmount = CurrencyConverter.convert(finalAmount, "TND", "TND", "f2ba7e8b2b225aff3756ea1a");
            project.setAmountinvestment(Float.parseFloat(convertedAmountInvestment));
            project.setFinalamount(Float.parseFloat(convertedFinalAmount));

            String qrCodeData = "Project Name: " + project.getName() + "\n" +
                    "Investment: " + convertedAmountInvestment + "\n" +
                    "Final Amount: " + convertedFinalAmount;
            String base64QRCodeImage = generateBase64QRCodeImage(qrCodeData);
            project.setQrCode(base64QRCodeImage);
        });

        model.addAttribute("listProj", listProj);
        return listProj;
    }

    //qrcode
    private String generateBase64QRCodeImage(String qrCodeData) {
        ByteArrayOutputStream stream = QRCode.from(qrCodeData).to(ImageType.PNG).stream();
        byte[] qrCodeBytes = stream.toByteArray();
        String base64QRCodeImage = Base64.getEncoder().encodeToString(qrCodeBytes);
        return base64QRCodeImage;
    }


    @GetMapping("/convert-to-euro/{projectName}")
    @ResponseBody
    public String getAmountInvestmentInEuro(@PathVariable String projectName) {
        Project project = projectService.findProjectByName(projectName);
        if (project != null) {
            double amountInvestment = project.getAmountinvestment();
            String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "TND", "EUR", "f2ba7e8b2b225aff3756ea1a");
            return "Le montant d'investissement dans le projet " + projectName + " en euros est " + convertedAmountInvestment;
        } else {
            return "Désolé, je n'ai pas trouvé de projet avec ce nom.";
        }
    }
    /* @GetMapping("/{projectId}/tax-report")
     public ResponseEntity<byte[]> generateTaxReport(@PathVariable int projectId) throws JRException, IOException {
         // Retrieve the project
         Optional<Project> optionalProject = projectRepository.findById(projectId);
         if (!optionalProject.isPresent()) {
             throw new MyResourceNotFoundException("Project not found with ID: " + projectId);
         }
         Project project = optionalProject.get();

         // Generate the tax report using JasperReports
         ClassPathResource reportResource = new ClassPathResource("reports/FiscalReport.jrxml");
         JasperDesign jasperDesign = JRXmlLoader.load(reportResource.getInputStream());
         JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
         JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(project));
         Map<String, Object> parameters = new HashMap<>();
         parameters.put("project", project);
         byte[] pdfBytes = JasperRunManager.runReportToPdf(jasperReport, parameters, dataSource);

         // Return the tax report as a PDF file
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_PDF);
         headers.setContentDispositionFormData("attachment", "tax-report.pdf");
         return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
     }

 */ int riskscore;


    /* @PostMapping("/add_PCinvestesment/{Fund-id}")
     public Project addProject(@RequestBody Project project, @PathVariable("Fund-id") Long idFund) throws MessagingException, IOException {
       LocalDate now = LocalDate.now();
         LocalDate startdate = project.getStartdate();
         if (startdate.isBefore(now) && startdate.getYear() == now.getYear()) {
             throw new IllegalArgumentException("Impossible d'ajouter ce projet à cette date.");
         }
         Project p = projectService.addProject(project, idFund);
         projectService.finalAmount();

         int riskscore = 0;
         String category = project.getCategory();
         LocalDate enddate = project.getEnddate();
         String country = project.getCountry();
         float amountinvestment = project.getAmountinvestment();

         // Ajouter des points de risque en fonction de la catégorie du projet
         if (category.equals("Animals")) {
             riskscore += 5;
         } else if (category.equals("Ecology")) {
             riskscore += 3;
         } else if (category.equals("Woman")) {
             riskscore += 4;
         }

         // Ajouter des points de risque en fonction de la durée du projet
         long days = ChronoUnit.DAYS.between(startdate, enddate);
         if (days > 365) {
             riskscore += 5;
         } else if (days > 180) {
             riskscore += 3;
         } else if (days > 90) {
             riskscore += 2;
         }

         // Ajouter des points de risque en fonction du montant d'investissement
         if (amountinvestment > 100000) {
             riskscore += 5;
         } else if (amountinvestment > 50000) {
             riskscore += 3;
         } else if (amountinvestment > 10000) {
             riskscore += 1;
         }

         // Ajouter des points de risque en fonction de la localisation du projet
         if (country.equals("Tunisie")) {
             riskscore += 3;
         } else if (country.equals("France")) {
             riskscore += 2;
         } else if (country.equals("Amerique")) {
             riskscore += 1;
         }

         p.setRiskscore(riskscore);

         // Lecture de l'image d'arrière-plan à partir du dossier des ressources
         //BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/p.png"));
         // Obtenir la largeur de l'image en pixels
         //int width = backgroundImage.getWidth();
         //System.out.println(width);
         String htmlContent = "<html><body style='margin:0; padding:0;'>" +
                 "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                 "<tr>" +
                 "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                 "<td></td>" +
                 "</tr>" +
                 "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                 "<td></td>" +
                 "</tr>" +
                 "<td background='cid:backgroundImage' bgcolor='#ffffff' valign='top' style='background-position: center top; background-size:cover; background-repeat: no-repeat; padding: 50px 15px;'>" +
                 "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                 "<tr>" +
                 "<td align='center' valign='bottom'>" +
                 "<h1 style='color:#FAEE4F;text-shadow: 1px 1px #000000;font-size:30;font-weight:bold;'>There's a new Project</h1>" +
                 "  <p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:20; font-weight:bold;'>" + p.getDescription() + "</p>" +
                 "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Amount of investment: </strong> " + p.getAmountinvestment() + "</p>" +
                 "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>If you want to contact us :</strong> </p>" +
                 "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Phone number :</strong> +216 55 555 555 : </p>" +
                 "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Mail :</strong> MIDAS@gmail.com: </p>" +


                 "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                 "<tr>" +
                 "<td align='center' style='padding: 20px 0;'>" +
                 "<table border='0' cellpadding='0' cellspacing='0'>" +
                 "<tr>" +
                 "<td align='center' bgcolor='#4169E1' style='border-radius: 3px;'>" +
                 "<a href='https://votre_site_web.com' target='_blank' style='font-size: 16px; font-family: Arial, sans-serif; color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 3px; display: block;'>Voir le projet</a>" +
                 "</td>" +
                 "</tr>" +
                 "</table>" +
                 "</td>" +
                 "</tr>" +
                 "</table>" +
                 "</td>" +
                 "</tr>" +
                 "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                 "<td></td>" +
                 "</tr>" +
                 "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                 "</tr>" +

                 "</table>" +
                 "</td>" +
                 "</tr>" +
                 "</table>" +
                 "</body></html>";

 // Création du message avec contenu HTML et image en pièce jointe
         MimeMessage message = javaMailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message, true);
         helper.setSubject("New Project!");
         helper.setFrom("chouchanecyrine@gmail.com");
         helper.setTo("cyrine.chouchane@esprit.tn");
         helper.setText(htmlContent, true);

 // Ajout de l'image en pièce jointe
         BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/try.png"));
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write(backgroundImage, "png", baos);
         ByteArrayDataSource backgroundDataSource = new ByteArrayDataSource(baos.toByteArray(), "image/png");
         helper.addInline("backgroundImage", backgroundDataSource);

 // Envoi du message
         javaMailSender.send(message);

         return p;
     }

 */
    @PostMapping("/add_PCinvestesment/{Fund-id}")
    public Project addProject(@RequestBody Project project, @PathVariable("Fund-id") Long idFund
    ) throws MessagingException, IOException {
        LocalDate now = LocalDate.now();
        LocalDate startdate = project.getStartdate();
        if (startdate.isBefore(now) && startdate.getYear() == now.getYear()) {
            throw new IllegalArgumentException("Impossible d'ajouter ce projet à cette date.");
        }
        String currency = "USD"; // spécifiez la devise ici (USD, EUR, TND)
        switch(currency) {
            case "USD":
                project.setCurrency(Currency.getInstance("USD"));
                break;
            case "EUR":
                project.setCurrency(Currency.getInstance("EUR"));
                break;
            case "TND":
                project.setCurrency(Currency.getInstance("TND"));
                break;
            default:
                throw new IllegalArgumentException("Devise non prise en charge.");
        }
        // Ajouter le champ de devise


        Project p = projectService.addProject(project, idFund);
        p.setCurrency(Currency.getInstance(currency)); // Stocker la devise dans le projet
        projectService.finalAmount();
        return p;
    }

    /*  @PostMapping("/add_PCinvestesment/{Fund-id}")
      public Project addProject(@RequestBody Project project, @PathVariable("Fund-id") Long idFund) throws MessagingException, IOException {
          LocalDate now = LocalDate.now();
        LocalDate startdate = project.getStartdate();
        if (startdate.isBefore(now) && startdate.getYear() == now.getYear()) {
            throw new IllegalArgumentException("Impossible d'ajouter ce projet à cette date.");
        }
          Project p = projectService.addProject(project, idFund);
          projectService.finalAmount();

          int riskscore = 0;
          String category = project.getCategory();
          LocalDate startdate = project.getStartdate();
          LocalDate enddate = project.getEnddate();
          String country = project.getCountry();
          float amountinvestment = project.getAmountinvestment();

          // Ajouter des points de risque en fonction de la catégorie du projet
          if (category.equals("Animals")) {
              riskscore += 5;
          } else if (category.equals("Ecology")) {
              riskscore += 3;
          } else if (category.equals("Woman")) {
              riskscore += 4;
          }

          // Ajouter des points de risque en fonction de la durée du projet
          long days = ChronoUnit.DAYS.between(startdate, enddate);
          if (days > 365) {
              riskscore += 5;
          } else if (days > 180) {
              riskscore += 3;
          } else if (days > 90) {
              riskscore += 2;
          }

          // Ajouter des points de risque en fonction du montant d'investissement
          if (amountinvestment > 100000) {
              riskscore += 5;
          } else if (amountinvestment > 50000) {
              riskscore += 3;
          } else if (amountinvestment > 10000) {
              riskscore += 1;
          }

          // Ajouter des points de risque en fonction de la localisation du projet
          if (country.equals("Tunisie")) {
              riskscore += 3;
          } else if (country.equals("France")) {
              riskscore += 2;
          } else if (country.equals("Amerique")) {
              riskscore += 1;
          }

          p.setRiskscore(riskscore);
          // Lecture de l'image d'arrière-plan à partir du dossier des ressources
          //BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/images/p.png"));
          // Obtenir la largeur de l'image en pixels
          //int width = backgroundImage.getWidth();
          //System.out.println(width);
          String htmlContent = "<html><body style='margin:0; padding:0;'>" +
                  "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                  "<tr>" +
                  "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                  "<td></td>" +
                  "</tr>" +
                  "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                  "<td></td>" +
                  "</tr>" +
                  "<td background='cid:backgroundImage' bgcolor='#ffffff' valign='top' style='background-position: center top; background-size:cover; background-repeat: no-repeat; padding: 50px 15px;'>" +
                  "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                  "<tr>" +
                  "<td align='center' valign='bottom'>" +
                  "<h1 style='color:#FAEE4F;text-shadow: 1px 1px #000000;font-size:30;font-weight:bold;'>There's a new Project</h1>" +
                  "  <p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:20; font-weight:bold;'>" + p.getDescription() + "</p>" +
                  "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:16px;'><strong>Amount of investment: </strong> " + p.getAmountinvestment() + "</p>" +
                  "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>If you want to contact us :</strong> </p>" +
                  "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Phone number :</strong> +216 55 555 555 : </p>" +
                  "<p style='color:#FFFFFF;text-shadow: 1px 1px #000000;font-size:17;'><strong>Mail :</strong> MIDAS@gmail.com: </p>" +


                  "<table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                  "<tr>" +
                  "<td align='center' style='padding: 20px 0;'>" +
                  "<table border='0' cellpadding='0' cellspacing='0'>" +
                  "<tr>" +
                  "<td align='center' bgcolor='#4169E1' style='border-radius: 3px;'>" +
                  "<a href='https://votre_site_web.com' target='_blank' style='font-size: 16px; font-family: Arial, sans-serif; color: #ffffff; text-decoration: none; padding: 15px 30px; border-radius: 3px; display: block;'>Voir le projet</a>" +
                  "</td>" +
                  "</tr>" +
                  "</table>" +
                  "</td>" +
                  "</tr>" +
                  "</table>" +
                  "</td>" +
                  "</tr>" +
                  "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                  "<td></td>" +
                  "</tr>" +
                  "<tr height='3px'>" + // Ajout d'une rangée vide avec une hauteur de 100px
                  "</tr>" +

                  "</table>" +
                  "</td>" +
                  "</tr>" +
                  "</table>" +
                  "</body></html>";

          // Envoi du message à l'utilisateur correspondant au projet en fonction de son âge
          String intevage = project.getIntevage().toString();
          int lowerAge = Integer.parseInt(intevage.substring(intevage.indexOf("_")+1, intevage.indexOf("_")+3));
          int upperAge = Integer.parseInt(intevage.substring(intevage.indexOf("_")+4, intevage.length()));
          List<User> userList = userService.getUsersByAgeRange(lowerAge, upperAge);
          for(User user : userList) {
              String email = user.getEmail();
              MimeMessage message = javaMailSender.createMimeMessage();
              MimeMessageHelper helper = new MimeMessageHelper(message, true);
              helper.setSubject("New Project!");
              helper.setFrom("chouchanecyrine@gmail.com");
              helper.setTo(email);
              helper.setText(htmlContent, true);

              javaMailSender.send(message);
          }

          return p;
      }
  */

    @GetMapping("/powerbi-report")
    public String getPowerBiReport() {
        return "<iframe title=\"projet pi bi\" width=\"1140\" height=\"541.25\" src=\"https://app.powerbi.com/reportEmbed?reportId=e8c0d175-d8cd-441a-963f-d51cd35812b1&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42\" frameborder=\"0\" allowFullScreen=\"true\"></iframe>";
    }


  /*  @GetMapping("/grant/{id}")
    public ResponseEntity<List<LocalDate>> getProjectDates(@PathVariable int id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<LocalDate> projectDates = List.of(project.getStartdate(), project.getEnddate());

        return new ResponseEntity<>(projectDates, HttpStatus.OK);
    }*/

    @GetMapping("/gantt-chart")
    public String generateGanttChart(Model model) {
        List<Project> projects = projectService.retrieveAllProject();


// Créer un tableau de dates distinctes pour l'axe X du diagramme
        Set<LocalDate> dateSet = new TreeSet<>();
        for (Project project : projects) {
            if (project.getStartdate() != null) {
                dateSet.add(project.getStartdate());
            }
            if (project.getEnddate() != null) {
                dateSet.add(project.getEnddate());
            }
        }
        List<LocalDate> dates = new ArrayList<>(dateSet);

// Créer un tableau HTML pour le diagramme de Gantt
        StringBuilder table = new StringBuilder();
        table.append("<table>\n");

// Ajouter une ligne pour les en-têtes
        table.append("<tr>\n");
        table.append("<th>Projet</th>\n");
        for (LocalDate date : dates) {
            table.append("<th>").append(date).append("</th>\n");
        }
        table.append("</tr>\n");

// Ajouter une ligne pour chaque projet
        for (Project project : projects) {
            table.append("<tr>\n");
            table.append("<td>").append(project.getName()).append("</td>\n");

            // Ajouter une cellule vide pour chaque date avant la date de début du projet
            int startIdx = dates.indexOf(project.getStartdate());
            for (int i = 0; i < startIdx; i++) {
                table.append("<td></td>\n");
            }

            // Ajouter une cellule colorée pour chaque jour que le projet est en cours
            int endIdx = dates.indexOf(project.getEnddate());
            for (int i = startIdx; i <= endIdx; i++) {
                table.append("<td style=\"background-color: #").append(project.getCategory()).append(";\"></td>\n");
            }

            // Ajouter une cellule vide pour chaque date après la date de fin du projet
            for (int i = endIdx + 1; i < dates.size(); i++) {
                table.append("<td></td>\n");
            }

            table.append("</tr>\n");
        }

        table.append("</table>\n");

        model.addAttribute("ganttChart", table.toString());

        return "gantt-chart";
    }
    /*   @GetMapping("/gantt")
       public ResponseEntity<Object> getProjectGanttChart() {
           List<Project> projects = projectRepository.findAll();

           List<Map<String, Object>> data = new ArrayList<>();
           for (Project project : projects) {
               Map<String, Object> item = new HashMap<>();
               item.put("id", project.getId());
               item.put("name", project.getName());
               item.put("category", project.getCategory());
               item.put("start", project.getStartdate());
               item.put("end", project.getEnddate());
               data.add(item);
           }

           Map<String, Object> result = new HashMap<>();
           result.put("data", data);

           return ResponseEntity.ok(result);
       }
   */
  /*  @GetMapping("/powerbi")
    public ModelAndView powerBiView() {
        ModelAndView modelAndView = new ModelAndView("powerbi");
        String iframeUrl = "https://app.powerbi.com/reportEmbed?reportId=f8b757e9-cd94-45da-96ef-e672b8d8f7cd&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42";
        String shareLink = "https://app.powerbi.com/links/g6L7kTA0ip?ctid=513486ec-6643-4f17-a508-76478311be42&pbi_source=linkShare";
        modelAndView.addObject("iframeUrl", iframeUrl);
        modelAndView.addObject("shareLink", shareLink);
        return modelAndView;
    }
*/
    @GetMapping("/powerbi")
    public ModelAndView powerBiView() {
        ModelAndView modelAndView = new ModelAndView("powerbi");
        modelAndView.addObject("reportUrl", "https://app.powerbi.com/reportEmbed?reportId=f8b757e9-cd94-45da-96ef-e672b8d8f7cd&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42");
        return modelAndView;
    }


}