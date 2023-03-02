package com.example.pi_back.Services;

import com.example.pi_back.Entities.Reclamation;

import java.util.List;

public interface ReclamationService {
    List<Reclamation> retrieveAllReclamation();
    Reclamation AddReclamation (Reclamation reclamation);
    void removeReclamation (int idReclamation);
    Reclamation retrieveReclamation (int idReclamation);
    Reclamation updateReclamation (Reclamation reclamation);
}
