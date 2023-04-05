package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Reclamation;
import com.example.pi_back.Entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {


}