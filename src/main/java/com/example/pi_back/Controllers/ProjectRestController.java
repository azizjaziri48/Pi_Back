package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.ProjectRepository;
import com.example.pi_back.Services.ProjectService;
import com.example.pi_back.config.MyResourceNotFoundException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.istack.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/Project")
public class ProjectRestController {
    private ProjectService ProjectService;
    private final ProjectRepository projectRepository;

    //Affichage de tous les projets
    /*@GetMapping("/retrieve-all-investesments")
    @ResponseBody
    public List<Project> getProject() {
        List<Project> listProj = ProjectService.retrieveAllProject();
        return listProj;
    }*/

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ProjectService projectService;


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

    @PostMapping("/add_PCinvestesment/{Fund-id}")
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
    }
        // Tableau et graphique pour afficher les informations sur les projets

/*    @GetMapping("/getTabInvest")
    public ResponseEntity<String> getAllProjects() {
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
*/


   /* @GetMapping("/retrieve-all-investments")
    @ResponseBody
    public List<Project> getAlllProjects() {
        List<Project> projects = ProjectService.retrieveAllProject();
        for (Project project : projects) {
            // Récupérer le nombre de "j'aime" et de "je n'aime pas" pour le projet
            int numLikes = project.getLikes().size();
            int numDislikes = project.getDislikes().size();
            // Ajouter les nombres de "j'aime" et "je n'aime pas" à la description du projet
            project.setDescription(project.getDescription() + "<br>" + numLikes + " j'aime, " + numDislikes + " je n'aime pas");
        }
        return projects;
    }
*/
    //Affichage de tous les projets
    @GetMapping("/retrieve-all-projects")
    public List<Project> getProject() {
        return ProjectService.retrieveAllProject();
    }


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
        return ProjectService.CalculateRateOfInves(idProject);
    }

    @GetMapping("/Rate/{amountinvestment}")
    @ResponseBody
    public double Rate(@PathVariable("amountinvestment") float AmountInvestestesment) {
        return ProjectService.Rate(AmountInvestestesment);
    }
    @GetMapping("/retrieve-all-likes")
    @ResponseBody
    public List<Project> getAlllProjects() {
        List<Project> projects = ProjectService.retrieveAllProject();
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
            document.add(new Paragraph("Age: " + project.getInvetage()));
            document.add(new Paragraph("Town: " + project.getTown()));


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


    //conversion behya sans qr
 /*  @GetMapping("/retrieve-all-investesments")
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
    private String generateBase64QRCodeImage(String qrCodeData) {
        ByteArrayOutputStream stream = QRCode.from(qrCodeData).to(ImageType.PNG).stream();
        byte[] qrCodeBytes = stream.toByteArray();
        String base64QRCodeImage = Base64.getEncoder().encodeToString(qrCodeBytes);
        return base64QRCodeImage;
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

*/
}
















