package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
}