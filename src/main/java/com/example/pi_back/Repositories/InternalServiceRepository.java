package com.example.pi_back.Repositories;


import com.example.pi_back.Entities.InternalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface InternalServiceRepository extends JpaRepository<InternalService, Integer> {
    @Query("select i from InternalService i where i.Sector = ?1")
    List<InternalService> findByActivitySector(String ActivitySector);
    @Query("select i from InternalService i where i.Date between ?1 and ?2")
    List<InternalService> findByEventDateBetween(LocalDate date1,LocalDate date2);
    InternalService findInternalServiceByCapacite(int capacite);

}