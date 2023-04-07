package com.example.pi_back.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pi_back.Entities.Credit;
@Repository
public interface CreditRepository extends CrudRepository<Credit, Long> {

    //Chercher les credits incomplets
    @Query("SELECT c FROM Credit c WHERE c.user.id= :iduser and c.completed = false and c.state = true ")
    List<Credit> getIncompletedCreditsByUser(@Param("iduser") Integer idUser);

    //Chercher si un User a un credit actif .
    @Query("SELECT c FROM Credit c WHERE c.user.id= :iduser and c.state = true ")
    List<Credit> getApprovedCreditsByUser(@Param("iduser") Integer idUser);


    //Chercher si un user a un credit actif .
    @Query("SELECT c FROM Credit c WHERE c.user.id= :iduser and c.state = true and c.completed=false ")
    Credit getActiveCreditsByUser(@Param("iduser") Integer idUser);

    //Chercher si un User a un credit actif .
    @Query(value="SELECT * FROM Credit c WHERE c.user_id= :iduser and c.state = true and c.completed =true limit 1 ",nativeQuery = true)
    Credit getlastCreditsByUser(@Param("iduser") Integer idUser);

    //selectionner le dernier credit complet pour tester son historique
    @Query(value = "SELECT * FROM credit c WHERE c.user_id=?1 and c.state=true and c.completed=true ORDER BY obtaining_date DESC limit 1" , nativeQuery =true)
    Credit getIDofLatestCompletedCreditsByUser(Integer idUser);

    @Query(value ="SELECT COUNT(c) FROM Credit c")
    long countCredits();
    @Query(value = "SELECT COUNT(c) FROM Credit c WHERE c.state = true")
    long countCreditsApproved();
    @Query(value = "SELECT COUNT(c) FROM Credit c WHERE c.state = true and c.completed = true")
    long countCreditsApprovedAndCompleted();
    @Query(value = "SELECT COUNT(c) FROM Credit c WHERE c.risk >= 0.0001 AND c.risk <= 0.9999")
    long countCreditsBonRisque();
    @Query(value = "SELECT COUNT(c) FROM Credit c WHERE c.risk >= 0.1001 AND c.risk <= 0.2499")
    long countCreditsRisqueModere();
    @Query(value = "SELECT COUNT(c) FROM Credit c WHERE c.risk >= 0.25")
    long countCreditsMauvaisRisque();



}