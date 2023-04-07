package com.example.pi_back.Services;

import java.util.List;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import javax.transaction.Transactional;

import com.example.pi_back.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pi_back.Entities.Credit;
import com.example.pi_back.Entities.DuesHistory;
import com.example.pi_back.Repositories.UserRepository;
import com.example.pi_back.Repositories.CreditRepository;
import com.example.pi_back.Repositories.DuesHistoryRepository;
@Service
public class DuesHistoryService implements IDuesHistoryService {

    @Autowired
    DuesHistoryRepository DHrepo;
    @Autowired
    CreditRepository Crepo;
    @Autowired
    CreditService CRService;
    @Autowired
    UserRepository User_repo;
    @Autowired
    private EmailSenderService emailService;



    @Override
    public List<DuesHistory> retrieveAllDuesHistorys() {
        return (List<DuesHistory>) DHrepo.findAll();
    }

    @Transactional
    public DuesHistory addDuesHistory(DuesHistory DH, Long idcredit) {
        Credit credit= Crepo.findById(idcredit).orElse(null);
        DH.setCredits(credit);
        //credit incomplete save the dues history
        if(DH.getCredits().getCompleted()==false)
        {
            //add supposed date ++
            if (DHrepo.getLast_date(idcredit).get(0)==null)
            { DH.setSupposedDate(java.sql.Date.valueOf(Instant.ofEpochMilli(credit.getMonthlyPaymentDate().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()));
            }
            else {
                DH.setSupposedDate(java.sql.Date.valueOf(Instant.ofEpochMilli(DHrepo.getLast_date(idcredit).get(0).getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().plusMonths(1)));
            }



            //calcul du montant total du credit
            float amount_topay=(CRService.Calcul_mensualite(credit)*(int) (credit.getCreditPeriod()*12));

            //compare payed amount with creditamount to pay
            if(amount_topay<=(PayedAmount(idcredit)+DH.getCredits().getMonthlyPaymentAmount()))
            {//add supposed date ++

                DH.getCredits().setCompleted(true);
                Crepo.save(credit);
                DH.getCredits().getUser().setCredit_authorization(true);
                User_repo.save(DH.getCredits().getUser());
                DHrepo.save(DH);
                emailService.sendFinishEmail(credit.getUser().getEmail(), DH);
            }
            else
            {
                DHrepo.save(DH);
            }

        }
        else
        {System.out.println("Le credit à été dejà payé");}

        return DH;
    }


    @Override
    public DuesHistory updateDuesHistory(DuesHistory DH, Long idcredit) {
        Credit credit= Crepo.findById(idcredit).orElse(null);
        DH.setCredits(credit);
        DHrepo.save(DH);
        return DH;
    }

    @Override
    public DuesHistory retrieveDuesHistory(Long idDuesHistory) {
        DuesHistory DH= DHrepo.findById(idDuesHistory) .orElse(null) ;
        return DH ;
    }



    @Override
    public void DeleteDuesHistory(Long idDuesHistory) {
        DHrepo.deleteById(idDuesHistory);
    }

    @Override
    public float PayedAmount(Long idcredit) {
        Credit credit= Crepo.findById(idcredit).orElse(null);
        List<DuesHistory> ListDH =DHrepo.getCredit_DuesHistory(idcredit);
        float payed_amount=ListDH.size()*CRService.Calcul_mensualite(credit);
        return payed_amount;
    }



    @Override
    public List<DuesHistory> retrieveAllDuesHistory_byCredit(Long idCredit) {
        return (List<DuesHistory>) DHrepo.getCredit_DuesHistory(idCredit);
    }

}
