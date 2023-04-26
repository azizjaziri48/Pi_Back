package com.example.pi_back.Services;

import com.example.pi_back.Entities.Reclamation;
import com.example.pi_back.Entities.Subject;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;



@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {

    private ReclamationRepository reclamationRepository;
    private final ReclamationSMSService smsService;


    @Autowired
private TransactionService transactionService;

    @Override
    public List<Reclamation> retrieveAllReclamation() {
        return reclamationRepository.findAll();
    }

    @Override
    public Reclamation AddReclamation(Reclamation reclamation) throws BadWordException {
        // Vérification de bad words dans la description
        String description = reclamation.getDescription().toLowerCase();
        List<String> badWords = getBadWords(); // récupération de la liste de bad words

        for (String word : badWords) {
            if (description.contains(word)) {
                throw new BadWordException("La description contient un mot interdit : " + word);
            }
        }

        // Si la description ne contient pas de bad words, enregistrer la réclamation
        return reclamationRepository.save(reclamation);
    }

    private List<String> getBadWords() {
        List<String> badWords = new ArrayList<>();

        try {
            File file = ResourceUtils.getFile("classpath:badwords.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line.trim().toLowerCase());
            }

            reader.close();
        } catch (IOException e) {
            // en cas d'erreur de lecture de fichier
            System.out.println("Erreur lors de la récupération des bad words : " + e.getMessage());
        }

        return badWords;
    }

    @Override
    public void removeReclamation(int idReclamation) {
        reclamationRepository.deleteById(idReclamation);
    }

    @Override
    public Reclamation retrieveReclamation(int idReclamation) {
        return reclamationRepository.findById(idReclamation).orElse(null);
    }

    @Override
    public Reclamation updateReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation respondToReclamation(int idReclamation, String reponse) throws MalformedURLException {
        Reclamation reclamation = retrieveReclamation(idReclamation);
        if (reclamation == null) {
            throw new IllegalArgumentException("La réclamation n'existe pas");
        }
        if (reclamation.getEtat()) {
            throw new IllegalArgumentException("La réclamation a déjà été traitée");
        }
        reclamation.setEtat(true);
        reclamation.setReponse(reponse);
        User user= reclamation.getTransaction().getAccount().getUser();
        smsService.sendSMS(user.getPhonenum().toString(),"Votre réclamation a été traitée "+reponse);
        return reclamationRepository.save(reclamation);
    }

    @Override
    public List<Reclamation> filterReclamationsBySubject(Subject subject) {
        return reclamationRepository.findBySubject(subject);
    }

     @Override
    public String getReclamationStatus(int idReclamation) {
        Reclamation reclamation = retrieveReclamation(idReclamation);
        if (reclamation == null) {
            return "La réclamation n'existe pas";
        } else if (reclamation.getEtat()) {
            return "La réclamation a été traitée : " + reclamation.getReponse();
        } else {
            return "La réclamation est en cours de traitement";
        }
    }




















}
