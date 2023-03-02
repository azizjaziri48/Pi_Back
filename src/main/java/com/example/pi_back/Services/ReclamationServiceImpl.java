package com.example.pi_back.Services;

import com.example.pi_back.Entities.Reclamation;
import com.example.pi_back.Repositories.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements ReclamationService{
    private ReclamationRepository ReclamationRepository;
    @Override
    public List<Reclamation> retrieveAllReclamation() {
        return ReclamationRepository.findAll();
    }

    @Override
    public Reclamation AddReclamation(Reclamation Reclamation) {
        return ReclamationRepository.save(Reclamation);
    }

    @Override
    public void removeReclamation(int idReclamation) {
        ReclamationRepository.deleteById(idReclamation);
    }

    @Override
    public Reclamation retrieveReclamation(int idReclamation) {
        return ReclamationRepository.findById(idReclamation).orElse(null);
    }

    @Override
    public Reclamation updateReclamation(Reclamation Reclamation) {
        return ReclamationRepository.save(Reclamation);
    }

}

