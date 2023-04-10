package com.example.pi_back.Services;
import com.example.pi_back.Controllers.CurrencyConverter;

import com.example.pi_back.Entities.Project;
import com.example.pi_back.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ChatBotService {
    @Autowired
    private ProjectRepository projectRepository;

    public ChatBotResponse generateResponse(ChatBotRequest request) {
        ChatBotResponse response = new ChatBotResponse();

        // Si l'utilisateur demande le risque d'investir dans un projet
        //Quel est le risque d'investissement dans le projet XYZ ?" ou "Le projet XYZ est-il risqué pour les investisseurs ?
        if (request.getMessage().toLowerCase().contains("risque") && request.getMessage().toLowerCase().contains("investir")) {
            Project project = projectRepository.findByName(request.getProjectName());
            if (project != null) {
                response.setMessage("Le risque d'investissement dans le projet " + project.getName() + " est de " + project.getRiskscore() + "/10.");
            } else {
                response.setMessage("Désolé, je n'ai pas trouvé de projet avec ce nom.");
            }
        }
//{ 	"message": "Quelles sont les coordonnées de la société  ?"
        // Si l'utilisateur demande les coordonnées de la société
        else if (request.getMessage().toLowerCase().contains("coordonnées") && request.getMessage().toLowerCase().contains("société")) {
            response.setMessage("Voici les coordonnées de la societe : +2165555555, mail:midas@gmail.Com");
        }

//{ 	"message": "Quelles sont les catégories disponibles pour investir?"
        // Si l'utilisateur demande les catégories disponibles pour investir
        else if (request.getMessage().toLowerCase().contains("catégories") && request.getMessage().toLowerCase().contains("investir")) {
            List<String> categories = projectRepository.findDistinctCategories();
            if (categories != null && !categories.isEmpty()) {
                response.setMessage("Voici les catégories disponibles pour investir: " + String.join(", ", categories));
            } else {
                response.setMessage("Désolé, je n'ai trouvé aucune catégorie pour investir.");
            }
        }
// Si l'utilisateur demande la conversion d'un projet en euros
        //Peux-tu me donner la conversion en euros de l'investissement pour le projet [nom du projet] ?
        else if (request.getMessage().toLowerCase().contains("conversion") && request.getMessage().toLowerCase().contains("dollars")) {
            String[] words = request.getMessage().toLowerCase().split(" ");
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals("projet") && i+1 < words.length) {
                    String projectName = words[i+1];
                    Project project = projectRepository.findByName(projectName);
                    if (project != null) {
                        BigDecimal amountInvestment = new BigDecimal(String.valueOf(project.getAmountinvestment()));
                        String convertedAmountInvestment = String.valueOf(CurrencyConverter.convert(amountInvestment, "TND", "USD", "f2ba7e8b2b225aff3756ea1a"));
                        response.setMessage("Le montant d'investissement dans le projet " + projectName + " en dollars est " + convertedAmountInvestment);
                    } else {
                        response.setMessage("Désolé, je n'ai pas trouvé de projet avec ce nom.");
                    }
                    break;
                }
            }
        }

        // Si l'utilisateur pose une autre question
        else {
            response.setMessage("Désolé, je n'ai pas compris votre question. Veuillez reformuler s'il vous plaît.");
        }

        return response;
    }
}
