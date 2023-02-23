package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
}