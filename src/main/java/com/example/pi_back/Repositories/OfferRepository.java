package com.example.pi_back.Repositories;

import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Partner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface OfferRepository extends JpaRepository<Offer, Integer> {

   @Query("select o from Offer o where o.partner.name = ?1")
    List<Offer> findByPartner_Name(String name);
   @Query("select o from Offer o where o.valeur=?1")
    Offer findOfferByValeur(Double val);

    @Query("select o from Offer o inner join o.users users where users.id = ?1")
    List<Offer> findByUsers_Id(int id);






}