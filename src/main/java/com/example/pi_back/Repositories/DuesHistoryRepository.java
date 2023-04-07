package com.example.pi_back.Repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.DuesHistory;
import com.example.pi_back.Entities.TypeAccount;

@Repository

public interface DuesHistoryRepository extends CrudRepository<DuesHistory, Long> {


    @Query("SELECT d FROM DuesHistory d WHERE d.credits.idCredit= :id_credit ")
    List<DuesHistory> getCredit_DuesHistory(@Param("id_credit") Long idcredit);

    @Query(value = "SELECT MAX(`supposed_date`) FROM `dues_history` WHERE `credits_id_credit`= :id_credit" , nativeQuery = true)
    List<Date> getLast_date(@Param("id_credit") Long idcredit);

}
