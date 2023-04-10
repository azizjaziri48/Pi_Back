package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Project;
import com.example.pi_back.Entities.ProjectStatistics;
import com.example.pi_back.Repositories.ProjectRepository;
import com.example.pi_back.Services.CalendarGeneratorService;
import com.example.pi_back.Services.ProjectPdfGenerator;
import com.example.pi_back.Services.ProjectService;
import com.example.pi_back.Services.UserService;
import com.example.pi_back.config.MyResourceNotFoundException;
import com.opencsv.CSVWriter;
import com.sun.istack.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import net.glxn.qrgen.QRCode;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


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
        csvWriter.writeNext(new String[]{"Id", "Name", "Description", "Amount Investment", "Investment Rate", "Intev Age", "Risk Score", "Country", "Start Date", "End Date", "Category", "Final Amount"});
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

    @GetMapping("/statistics")
    public ResponseEntity<ProjectStatistics> getProjectStatistics() {
        List<Project> projects = projectRepository.findAll();

        int totalProjects = projects.size();

        BigDecimal totalAmountInvestment = projects.stream()
                .map(Project::getAmountinvestment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageTauxInvest = projects.stream()
                .map(Project::getTauxinvest)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(totalProjects), 2, RoundingMode.HALF_UP);

        int totalLikes = projects.stream()
                .mapToInt(project -> project.getLikes().size())
                .sum();

        int totalDislikes = projects.stream()
                .mapToInt(project -> project.getDislikes().size())
                .sum();

        ProjectStatistics statistics = new ProjectStatistics(totalProjects, totalAmountInvestment,
                averageTauxInvest, totalLikes, totalDislikes);

        return ResponseEntity.ok(statistics);
    }




   /* @PostMapping("/add_PCinvestesment/{Fund-id}")
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
               // Calcul du score de risque en fonction du montant d'investissement
        BigDecimal amount = project.getAmountinvestment();
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


//affichage aadi
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




    //suppression d'un projet

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> removeProject(@PathVariable("id") Integer idProject) {
        if (ProjectService.retrieveProject(idProject) == null) {
            return new ResponseEntity<>("The Project to be deleted doesn't exist", HttpStatus.NOT_FOUND);
        }


        ProjectService.removeProject(idProject);
        return new ResponseEntity<>("Project was deleted successfully", HttpStatus.OK);
    }

    //Affichage d'un seul projet

   /* @GetMapping("/get/{id}")
    ResponseEntity<Project> retrieveProject(@PathVariable("id") Integer idProject) {
        Project Retrieved_Project = ProjectService.retrieveProject(idProject);
        if (Retrieved_Project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Retrieved_Project, HttpStatus.OK);

    }*/
   @GetMapping("/retrieve-project/{id}")
   @ResponseBody
   public ResponseEntity<Project> retrieveProject(@PathVariable("id") Integer idProject) {
       Project project = projectService.retrieveProject(idProject);
       if (project == null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }

       BigDecimal amountInvestment = new BigDecimal(String.valueOf(project.getAmountinvestment()));
       BigDecimal finalAmount = new BigDecimal(String.valueOf(project.getFinalamount()));
       String convertedAmountInvestment = String.valueOf(CurrencyConverter.convert(amountInvestment, "TND", "EUR", "f2ba7e8b2b225aff3756ea1a"));
       String convertedFinalAmount = String.valueOf(CurrencyConverter.convert(finalAmount, "TND", "EUR", "f2ba7e8b2b225aff3756ea1a"));
       project.setAmountinvestment(new BigDecimal(convertedAmountInvestment));
       project.setFinalamount(new BigDecimal(convertedFinalAmount));

       String qrCodeData = "Project Name: " + project.getName() + "\n" +
               "Investment: " + convertedAmountInvestment + "\n" +
               "Final Amount: " + convertedFinalAmount;
       String base64QRCodeImage = generateBase64QRCodeImage(qrCodeData);
       project.setQrCode(base64QRCodeImage);

       return new ResponseEntity<>(project, HttpStatus.OK);
   }


    //Mise à jour des projets
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
    public BigDecimal CalculateRateOfInves(@PathVariable("Project-id") int idProject) {
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


//conversion normal aadeya

  @GetMapping("/convert/{amount}/{fromCurrency}/{toCurrency}")
    public String convertCurrency(@PathVariable double amount, @PathVariable String fromCurrency, @PathVariable String toCurrency) {
        String apiKey = "f2ba7e8b2b225aff3756ea1a"; // Remplacez par votre propre API key
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return result;
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
            BigDecimal amountInvestment = project.getAmountinvestment();
            BigDecimal finalAmount = project.getFinalamount();
            String convertedAmountInvestment = String.valueOf(CurrencyConverter.convert(amountInvestment, "TND", "USD", apiKey));
            String convertedFinalAmount = String.valueOf(CurrencyConverter.convert(finalAmount, "USD", "TND", apiKey));
            project.setAmountinvestment(new BigDecimal(convertedAmountInvestment));
            project.setFinalamount(new BigDecimal(convertedFinalAmount));
            projectService.updateProject(project);
        }

        return "La conversion de devises a été effectuée avec succès pour tous les projets.";
    }


    @GetMapping("/convert-currency/{projectId}")
    @ResponseBody
    public String convertCurrency(@PathVariable int projectId) {
        Project project = projectService.retrieveProjectById(projectId);
        String apiKey = "f2ba7e8b2b225aff3756ea1a"; // votre clé API ici

        BigDecimal amountInvestment = project.getAmountinvestment();
        BigDecimal finalAmount = project.getFinalamount();
        String convertedAmountInvestment = String.valueOf(CurrencyConverter.convert(amountInvestment, "TND", "USD", apiKey));
        String convertedFinalAmount = String.valueOf(CurrencyConverter.convert(finalAmount, "USD", "TND", apiKey));

        // Remplacer le point par une virgule et formater le résultat avec 4 décimales
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRENCH);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,##0.0000", symbols);
        convertedAmountInvestment = df.format(new BigDecimal(convertedAmountInvestment));
        convertedFinalAmount = df.format(new BigDecimal(convertedFinalAmount));

        return "Conversion de la devise pour le projet " + project.getName() + ": " + "amountinvestment = " + convertedAmountInvestment + ", finalamount = " + convertedFinalAmount;
    }






//jasper report

    @GetMapping("/generate-report")
    public void generateReport(HttpServletResponse response) throws JRException, IOException {

        // Chargement du modèle de rapport JasperReport (.jrxml)
        JasperDesign jasperDesign = JRXmlLoader.load(new ClassPathResource("/reports/try.jrxml").getInputStream());

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
    BigDecimal amountInvestment = BigDecimal.valueOf(project.getAmountinvestment());
    BigDecimal finalAmount = BigDecimal.valueOf(project.getFinalamount());
    String convertedAmountInvestment = CurrencyConverter.convert(amountInvestment, "TND", "TND", "f2ba7e8b2b225aff3756ea1a");
    String convertedFinalAmount = CurrencyConverter.convert(finalAmount, "TND", "TND", "f2ba7e8b2b225aff3756ea1a");
    project.setAmountinvestment(new BigDecimal(convertedAmountInvestment));
    project.setFinalamount(new BigDecimal(convertedFinalAmount));

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
    public List<Project> getAllProjects(Model model) {
        List<Project> listProj = projectService.retrieveAllProject();



// Conversion des montants
        listProj.forEach(project -> {
            BigDecimal amountInvestment = new BigDecimal(String.valueOf(project.getAmountinvestment()));
            BigDecimal finalAmount = new BigDecimal(String.valueOf(project.getFinalamount()));
            String convertedAmountInvestment = String.valueOf(CurrencyConverter.convert(amountInvestment, "TND", "TND", "f2ba7e8b2b225aff3756ea1a"));
            String convertedFinalAmount = String.valueOf(CurrencyConverter.convert(finalAmount, "TND", "TND", "f2ba7e8b2b225aff3756ea1a"));
            project.setAmountinvestment(new BigDecimal(convertedAmountInvestment));
            project.setFinalamount(new BigDecimal(convertedFinalAmount));

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





 int riskscore;


    @PostMapping("/add_PCinvestesment/{Fund-id}")
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
        BigDecimal amountinvestment = project.getAmountinvestment();

        // Ajouter des points de risque en fonction de la catégorie du projet
        if (category.equals("Animals")) {
            riskscore += 5;
        } else if (category.equals("Ecology")) {
            riskscore += 3;
        } else if (category.equals("Woman")) {
            riskscore += 4;
        } else if (category.equals("Humanism")) {
            riskscore += 2;
        }  else if (category.equals("Real Estate")) {
            riskscore += 2;
        }
        System.out.println(riskscore);

        // Ajouter des points de risque en fonction de la durée du projet
        long days = ChronoUnit.DAYS.between(startdate, enddate);
        if (days > 365) {
            riskscore += 5;
        } else if (days > 180) {
            riskscore += 3;
        } else if (days > 90) {
            riskscore += 2;
        }
        System.out.println(riskscore);
        // Ajouter des points de risque en fonction du montant d'investissement
        if (amountinvestment.compareTo(new BigDecimal("100000")) > 0) {
            riskscore += 5;
        } else if (amountinvestment.compareTo(new BigDecimal("50000")) > 0) {
            riskscore += 3;
        } else if (amountinvestment.compareTo(new BigDecimal("10000")) > 0) {
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


 /*@PostMapping("/add_PCinvestesment/{Fund-id}")
   public Project addProject(@RequestBody Project project, @PathVariable("Fund-id") Long idFund
   ) throws MessagingException, IOException {
       LocalDate now = LocalDate.now();
       LocalDate startdate = project.getStartdate();
       if (startdate.isBefore(now) && startdate.getYear() == now.getYear()) {
           throw new IllegalArgumentException("Impossible d'ajouter ce projet à cette date.");
       }
       String currency = "USD"; // spécifiez la devise ici (USD, EUR, TND)
       switch (currency) {
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
       int riskscore = 0;
       String category = project.getCategory();
       LocalDate enddate = project.getEnddate();
       String country = project.getCountry();
       BigDecimal amountinvestment = project.getAmountinvestment();

       // Ajouter des points de risque en fonction de la catégorie du projet
       if (category.equals("Animals")) {
           riskscore += 5;
       } else if (category.equals("Ecology")) {
           riskscore += 3;
       } else if (category.equals("Woman")) {
           riskscore += 4;
       } else if (category.equals("Humanism")) {
           riskscore += 2;
       }  else if (category.equals("Real Estate")) {
        riskscore += 2;
    }
       System.out.println(riskscore);

       // Ajouter des points de risque en fonction de la durée du projet
           long days = ChronoUnit.DAYS.between(startdate, enddate);
           if (days > 365) {
               riskscore += 5;
           } else if (days > 180) {
               riskscore += 3;
           } else if (days > 90) {
               riskscore += 2;
           }
System.out.println(riskscore);
           // Ajouter des points de risque en fonction du montant d'investissement
           if (amountinvestment.compareTo(new BigDecimal("100000")) > 0) {
               riskscore += 5;
           } else if (amountinvestment.compareTo(new BigDecimal("50000")) > 0) {
               riskscore += 3;
           } else if (amountinvestment.compareTo(new BigDecimal("10000")) > 0) {
               riskscore += 1;
           }
           System.out.println("Amount investissement :"+amountinvestment);
           BigDecimal test = BigDecimal.valueOf(amountinvestment.compareTo(new BigDecimal("7000")));
           System.out.println("Compare = "+test);
       System.out.println("Amount Big :"+new BigDecimal("7000"));
       System.out.println(riskscore);

           // Ajouter des points de risque en fonction de la localisation du projet
           if (country.equals("Tunisie")) {
               riskscore += 3;
           } else if (country.equals("France")) {
               riskscore += 2;
           } else if (country.equals("Amerique")) {
               riskscore += 1;
           }
       System.out.println(riskscore);

           p.setRiskscore(riskscore);
           return p;
       }
*/
    /*@PostMapping("/add_PCinvestesment/{Fund-id}")
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
          BigDecimal amountinvestment = project.getAmountinvestment();

          // Ajouter des points de risque en fonction de la catégorie du projet
         if (category.equals("Animals")) {
             riskscore += 5;
         } else if (category.equals("Ecology")) {
             riskscore += 3;
         } else if (category.equals("Woman")) {
             riskscore += 4;
         } else if (category.equals("Humanism")) {
             riskscore += 2;
         }  else if (category.equals("Real Estate")) {
             riskscore += 2;
         }
         System.out.println(riskscore);

         // Ajouter des points de risque en fonction de la durée du projet
         long days = ChronoUnit.DAYS.between(startdate, enddate);
         if (days > 365) {
             riskscore += 5;
         } else if (days > 180) {
             riskscore += 3;
         } else if (days > 90) {
             riskscore += 2;
         }
         System.out.println(riskscore);
         // Ajouter des points de risque en fonction du montant d'investissement
         if (amountinvestment.compareTo(new BigDecimal("100000")) > 0) {
             riskscore += 5;
         } else if (amountinvestment.compareTo(new BigDecimal("50000")) > 0) {
             riskscore += 3;
         } else if (amountinvestment.compareTo(new BigDecimal("10000")) > 0) {
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

   /*    @GetMapping("/powerbi-report")
       public String getPowerBiReport () {
           return "<iframe title=\"projet pi bi\" width=\"1140\" height=\"541.25\" src=\"https://app.powerbi.com/reportEmbed?reportId=e8c0d175-d8cd-441a-963f-d51cd35812b1&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42\" frameborder=\"0\" allowFullScreen=\"true\"></iframe>";
       }*/
   @GetMapping("/powerbi")
   public ModelAndView powerBiView() {
       ModelAndView modelAndView = new ModelAndView("powerbi");
       modelAndView.addObject("reportUrl", "https://app.powerbi.com/reportEmbed?reportId=f8b757e9-cd94-45da-96ef-e672b8d8f7cd&autoAuth=true&ctid=513486ec-6643-4f17-a508-76478311be42");
       return modelAndView;
   }

   @GetMapping("/grant/{id}")
    public ResponseEntity<List<LocalDate>> getProjectDates(@PathVariable int id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<LocalDate> projectDates = List.of(project.getStartdate(), project.getEnddate());

        return new ResponseEntity<>(projectDates, HttpStatus.OK);
    }


   @GetMapping("/gantt")
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


    @GetMapping("/pdf")
    public ResponseEntity<Resource> generatePdf() {
        List<Project> projects = projectService.getAllProjects();
// create an instance of CalendarGeneratorService
        CalendarGeneratorService calendarGeneratorService = new CalendarGeneratorService();

// create an instance of ProjectPdfGenerator with the projects and calendarGeneratorService
        ProjectPdfGenerator pdfGenerator = new ProjectPdfGenerator(projects);

        try {
            String fileName = "liste_projets.pdf";
            String filePath = "C:/Users/ASUS TUF I5/Desktop/hassine/hassine/pdf" + fileName;
            pdfGenerator.generatePdf(filePath); // Passer le chemin d'accès et le nom de fichier
            byte[] pdfContent = Files.readAllBytes(Paths.get(filePath));
            ByteArrayResource resource = new ByteArrayResource(pdfContent);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}






























