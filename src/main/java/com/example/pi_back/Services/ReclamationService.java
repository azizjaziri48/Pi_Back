package com.example.pi_back.Services;

import com.example.pi_back.Entities.Reclamation;
import com.example.pi_back.Entities.Subject;

import java.io.IOException;
import java.util.List;

public interface ReclamationService {
    List<Reclamation> retrieveAllReclamation();
    public Reclamation AddReclamation(Reclamation reclamation) throws IOException;
    void removeReclamation (int idReclamation);
    Reclamation retrieveReclamation (int idReclamation);
    Reclamation updateReclamation (Reclamation reclamation);

   //List<Reclamation> filterReclamationsBySubject(Subject subject);

    Reclamation respondToReclamation(int idReclamation, String reponse); //admin testée

}
