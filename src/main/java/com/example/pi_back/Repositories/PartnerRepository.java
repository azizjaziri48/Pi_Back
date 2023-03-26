package com.example.pi_back.Repositories;


import com.example.pi_back.Entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Integer> {
  @Query("select o.partner from Offer o group by o.partner.id order by count(o.id) desc")
  List<Partner> findMostFrequentPartner();
  @Query("select o.partner from Offer o group by o.partner.id order by count(o.id) ASC ")
  List<Partner> findLessFrequentPartner();
  @Query("select p from Partner p LEFT JOIN Offer o on p.id = o.partner.id where o.partner.id IS NULL")
  List<Partner> findPartnersWithoutOffers();

  @Query("SELECT COUNT(DISTINCT o.partner.id) FROM Offer o")
  Long countPartnersWithOffers();


}