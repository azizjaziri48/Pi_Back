package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
}