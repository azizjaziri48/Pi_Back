package com.example.pi_back.Services;

import com.example.pi_back.Entities.*;
import com.example.pi_back.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;


@Service
public class CreditService implements ICreditService {
    @Autowired
    GarantorRepository GRrepo;
    @Autowired
    CreditRepository Crepo;
    @Autowired
    UserRepository UserRepo;
    @Autowired
    FundRepository FundRepo;

    @Autowired
    DuesHistoryRepository DHrepo;

    @Autowired
    private EmailSenderService emailService;
    @Autowired
    private ExcelEmailSender excelSender;

    @Override
    public List<Credit> retrieveAllCredits() {
        return (List<Credit>) Crepo.findAll();
    }




    //completed par default false

    @Override
    public Credit addCredit(Credit credit, Integer Id_user, Long Id_fund ,Long Id_garantor) {
        User user = UserRepo.findById(Id_user).orElse(null);
        Fund fund=FundRepo.findById(Id_fund).orElse(null);
        Garantor Garant= GRrepo.findById(Id_garantor).orElse(null);
        credit.setGarantor(Garant);
        credit.setUser(user);
        credit.setFunds(fund);
        credit.setDateDemande(new Date());
        //NEW USER
        if(user.getCredit_authorization()==null)
        {   //tester sur le risk(user nouveau)
            //NB LE TAUX DE RISQUE 1%<R<2.5%
            if(1.5*credit.getGarantor().getSalaryGarantor()>=credit.getAmount())
            {
                //CALCUL RISK
                credit.setRisk((float) (0.01+credit.getAmount()/(credit.getGarantor().getSalaryGarantor()*100)));
                Acceptation(credit,fund,"Nouveau User avec garant certifié");
                try {
                    List<Amortissement> amort = Arrays.asList(TabAmortissementTauxFix(credit));
                    emailService.sendEmailWithExcelAttachment(user.getEmail(), credit, user, amort);
                } catch (IOException e) {
                    // Traiter l'exception IO
                    e.printStackTrace();
                } catch (MessagingException e) {
                    // Traiter l'exception de messagerie
                    e.printStackTrace();
                }

            }
            else
            {
                credit.setState(false);
                credit.setReason("Salaire garant insuffisant il doit etre egale à 0.33*montant du crédit");
                emailService.sendGarantEmail(user.getEmail(), credit, user);

            }

        }
        //EXISTING USER
        else if(user.getCredit_authorization()==true)
        {      //Ratio retard=late_days/period_of credit
            float Ratio_retard=(CaculateLateDays(Crepo.getIDofLatestCompletedCreditsByUser(Id_user)))/(Crepo.getIDofLatestCompletedCreditsByUser(Id_user).getCreditPeriod()*12*30);
            //3 CAS
            if (Ratio_retard<0.1)
            {credit.setRisk((float) 0.1);
                Acceptation(credit,fund,"Ancien user avec un BON risque ");
                try {
                    List<Amortissement> amort = Arrays.asList(TabAmortissementTauxFix(credit));
                    emailService.sendEmailWithExcelAttachment(user.getEmail(), credit, user, amort);
                } catch (IOException e) {
                    // Traiter l'exception IO
                    e.printStackTrace();
                } catch (MessagingException e) {
                    // Traiter l'exception de messagerie
                    e.printStackTrace();
                }
            }
            else if (Ratio_retard>=0.1 && Ratio_retard<=0.25)
            {credit.setRisk(Ratio_retard);
                Acceptation(credit,fund,"Ancien user avec un risque modérable ");
                try {
                    List<Amortissement> amort = Arrays.asList(TabAmortissementTauxFix(credit));
                    emailService.sendEmailWithExcelAttachment(user.getEmail(), credit, user, amort);
                } catch (IOException e) {
                    // Traiter l'exception IO
                    e.printStackTrace();
                } catch (MessagingException e) {
                    // Traiter l'exception de messagerie
                    e.printStackTrace();
                }
            }
            else
            {credit.setState(false);
                credit.setReason("User trop Risqué Mauvais Historique");
                user.setCredit_authorization(false);	//blackLIster le user
                UserRepo.save(user);
                System.out.println("user blacklister");
                emailService.sendRefusEmail(user.getEmail(), user);

            }

        }
        //BLACK LISTED USER
        else
        {
            credit.setState(false);
            credit.setReason("Interdiction de Crédit");
            emailService.sendRefusEmail(user.getEmail(), user);
        }
        Crepo.save(credit);
        return credit;

    }


    //fonction de calcul du taux d'interet et de tranchement de la somme du fond
    public void Acceptation(Credit  credit ,Fund fund ,String msg) {

        //CALCUL DU TAUX D'INTERET
        credit.setInterestRate(credit.getRisk()+fund.getTauxFund()+fund.getTauxGain());
        //changement du fond de la banque
        if(fund.getAmountFund()-credit.getAmount()>0)
        {
            fund.setAmountFund(fund.getAmountFund()-credit.getAmount());
            credit.setState(true);
            Date date=new Date(System.currentTimeMillis());
            credit.setObtainingDate(date);

            if(credit.getDiffere()==false)
            { //System.out.println(Calcul_mensualite(credit));
                credit.setMonthlyPaymentAmount(Calcul_mensualite(credit));
                credit.setReason(msg);
                credit.getUser().setCredit_authorization(false);
                credit.setCompleted(false);
                FundRepo.save(fund);

            }
            //si credit avec différé
            else if(credit.getDiffere()==true)
            {   credit.setAmount(credit.getAmount()+(1+(credit.getCreditPeriod()*credit.getInterestRate())));
                credit.setMonthlyPaymentAmount(Calcul_mensualite(credit));
                //changement de la date de paiement prevue selon la periode du differe

                credit.setMonthlyPaymentDate(java.sql.Date.valueOf(Instant.ofEpochMilli(credit.getMonthlyPaymentDate().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().plusMonths((long) (credit.getDiffPeriod()*12))));
                credit.setReason(msg);
                credit.getUser().setCredit_authorization(false);
                credit.setCompleted(false);
                FundRepo.save(fund);
            }


        }
        else
        {credit.setState(false);
            credit.setCompleted(false);
            credit.setReason("fond insuffisant");}

    }

    /************************************************************************************************************/

    //Fonction qui calcule les retards enregistré dans le history dues
    public int CaculateLateDays(Credit  cr) {

        int _MS_PER_DAY = 1000 * 60 * 60 * 24;
        int S=0;

        List<DuesHistory> ListDH =DHrepo.getCredit_DuesHistory(cr.getIdCredit());
        for (DuesHistory DH : ListDH) {
            Date end=DH.getDateHistory();
            Date begin=DH.getSupposedDate();
            //difference entre deux dates en jours
            int diffInDays = (int)( (end.getTime() - begin.getTime())
                    / (1000 * 60 * 60 * 24) );
            S=S+(diffInDays);


        }
        return S;

    }

    /************************************************************************************************************/

    //Fonction qui calcule la mensualité
    public float Calcul_mensualite(Credit cr)
    {
        float montant=cr.getAmount();
        float tauxmensuel=cr.getInterestRate()/12;
        float period=cr.getCreditPeriod()*12;
        float mensualite=(float) ((montant*tauxmensuel)/(1-(Math.pow((1+tauxmensuel),-period ))));
                                 //interet
        return mensualite;
    }

    /************************************************************************************************************/
    //Fonction qui calcule le tab Amortissement Taux Fixe
    public Amortissement[] TabAmortissementTauxFix(Credit cr)
    {


        double interest=cr.getInterestRate()/12;
        System.out.println("intereeeeeeeeeeeettt");
        System.out.println(cr.getInterestRate());
        int leng=(int) (cr.getCreditPeriod()*12);
        System.out.println("perioddeeeeeeee");
        System.out.println(cr.getCreditPeriod()*12);
        System.out.println(leng);

        Amortissement[] ListAmortissement =new Amortissement[leng];

        Amortissement amort=new Amortissement() ;
        //System.out.println(cr.getAmount());


        amort.setMontantR(cr.getAmount());
        amort.setMensualite(Calcul_mensualite(cr));
        amort.setInterest(amort.getMontantR()*interest);
        amort.setAmortissement(amort.getMensualite()-amort.getInterest());
        ListAmortissement[0]=amort;

        //System.out.println(ListAmortissement[0]);
        for (int i=1;i< cr.getCreditPeriod()*12;i++) {
            Amortissement amortPrecedant=ListAmortissement[i-1];
            Amortissement amortNEW=new Amortissement() ;
            amortNEW.setMontantR(amortPrecedant.getMontantR()-amortPrecedant.getAmortissement());
            amortNEW.setInterest(amortNEW.getMontantR()*interest);
            amortNEW.setMensualite(Calcul_mensualite(cr));
            amortNEW.setAmortissement(amortNEW.getMensualite()-amortNEW.getInterest());
            ListAmortissement[i]=amortNEW;

        }

        return ListAmortissement;
    }

    /************************************************************************************************************/

    public Amortissement[] TabAmortissementTauxVariable(Credit cr) {
        double interest;
        double mensualite, amortisation;
        float montantR = cr.getAmount();
        float tauxMensuel = cr.getInterestRate() / 12;
        int length = (int) (cr.getCreditPeriod() * 12);
        Amortissement[] ListAmortissement = new Amortissement[length];
        for (int i = 0; i < length; i++) {
            Amortissement amort = new Amortissement();
            amort.setMontantR(montantR);
            interest = montantR * tauxMensuel;
            mensualite = Calcul_mensualite(cr);
            amort.setMensualite(mensualite);
            amort.setInterest(interest);
            if (i == length - 1) {
                amortisation = montantR;
            } else {
                amortisation = mensualite - interest;
            }
            amort.setAmortissement(amortisation);
            montantR -= amortisation;
            ListAmortissement[i] = amort;
        }
        return ListAmortissement;
    }


    /************************************************************************************************************/


    public Amortissement[] TabAmortissementInteretConstant(Credit cr) {
        double interest = cr.getInterestRate() / 12;
        float montantR = cr.getAmount();
        int length = (int) (cr.getCreditPeriod() * 12);
        Amortissement[] ListAmortissement = new Amortissement[length];
        for (int i = 0; i < length; i++) {
            Amortissement amort = new Amortissement();
            amort.setMontantR(montantR);
            amort.setMensualite(Calcul_mensualite(cr));
            amort.setInterest(cr.getAmount() * interest);
            double amortisation = amort.getMensualite() - amort.getInterest();
            amort.setAmortissement((float) amortisation);
            montantR -= (float) amortisation;
            ListAmortissement[i] = amort;
        }
        return ListAmortissement;
    }


    public static List<Map<String, Double>> tableauAmortissement(double montantRestant, double tauxInteretAnnuel, double mensualite, int dureeAnnees) {
        double tauxInteretMensuel = tauxInteretAnnuel / 12;
        int dureeMois = dureeAnnees * 12;
        double amortissement, interet, nouvelleDette;
        List<Map<String, Double>> tableau = new ArrayList<>();
        for (int i = 1; i <= dureeMois; i++) {
            Map<String, Double> ligne = new HashMap<>();
            interet = montantRestant * tauxInteretMensuel;
            amortissement = mensualite - interet;
            nouvelleDette = montantRestant - amortissement;
            if (nouvelleDette < 0) {
                amortissement = montantRestant;
                nouvelleDette = 0;
                mensualite = interet + amortissement;
            }
            ligne.put("mois", (double) i);
            ligne.put("montantRestant", montantRestant);
            ligne.put("interet", interet);
            ligne.put("amortissement", amortissement);
            ligne.put("mensualite", mensualite);
            montantRestant = nouvelleDette;
            tableau.add(ligne);
        }
        return tableau;
    }


    public Amortissement[] TabAmortissementAmortConst(Credit cr) {

        double interest = cr.getInterestRate() / 12;
        double amortissement = cr.getAmount() / (cr.getCreditPeriod() * 12);
        int length = (int) (cr.getCreditPeriod() * 12);

        Amortissement[] ListAmortissement = new Amortissement[length];
        double montantR = cr.getAmount();
        double mensualite = amortissement + montantR * interest;

        for (int i = 0; i < length; i++) {
            Amortissement amort = new Amortissement();
            amort.setMontantR(montantR);
            amort.setMensualite(mensualite);
            amort.setInterest(montantR * interest);
            amort.setAmortissement(amortissement);
            ListAmortissement[i] = amort;

            montantR -= amortissement;
            mensualite = amortissement + montantR * interest;
        }

        return ListAmortissement;
    }


    public Amortissement[] TabAmortissementInteretConst(Credit cr) {
        double interest = cr.getInterestRate() / 12;
        double amortissement = (cr.getAmount() * interest) / (1 - Math.pow(1 + interest, -(cr.getCreditPeriod() * 12)));
        int length = (int) (cr.getCreditPeriod() * 12);

        Amortissement[] ListAmortissement = new Amortissement[length];
        double montantR = cr.getAmount();
        double mensualite = amortissement;

        for (int i = 0; i < length; i++) {
            Amortissement amort = new Amortissement();
            amort.setMontantR(montantR);
            amort.setMensualite(mensualite);
            amort.setInterest(montantR * interest);
            amort.setAmortissement(amortissement);
            ListAmortissement[i] = amort;

            montantR -= amortissement;
        }

        return ListAmortissement;
    }

    @Override
    public Amortissement Simulateur(Credit credit)

    {   System.out.println(credit.getAmount());
        Amortissement simulator =new Amortissement();
        //mnt total
        simulator.setMontantR(0);

        //mnt interet
        simulator.setInterest(0);
        //mnt monthly
        simulator.setMensualite(Calcul_mensualite(credit));

        Amortissement[] Credittab = TabAmortissementTauxFix(credit);
        float s=0;
        float s1=0;
        for (int i=0; i < Credittab.length; i++) {
            s1=(float) (s1+Credittab[i].getInterest());
        }
        //mnt interet
        simulator.setInterest(s1);
        //mnt total
        simulator.setAmortissement(credit.getAmount()+s1);
        //mnt credit
        simulator.setMontantR(credit.getAmount());





        return simulator;

    }

    @Override
    public Credit updateCredit(Credit credit, Integer Id_user, Long Id_fund) {
        User user= UserRepo.findById(Id_user).orElse(null);
        Fund fund=FundRepo.findById(Id_fund).orElse(null);
        credit.setUser(user);
        credit.setFunds(fund);
        Crepo.save(credit);
        return credit;

    }

    @Override
    public Credit retrieveCredit(Long idCredit) {
        Credit credit= Crepo.findById(idCredit) .orElse(null) ;
        return credit ;
    }

    public Credit findCreditById(Long idCredit) {
        Optional<Credit> optionalCredit = creditRepository.findById(idCredit);
        if (optionalCredit.isPresent()) {
            return optionalCredit.get();
        }
        throw new EntityNotFoundException("Credit with id " + idCredit + " not found");
    }

    @Override
    public void DeleteCredit(Long id) {
        Crepo.deleteById(id);
    }

    @Override
    public Credit ArchiveCredit(Long idCredit) {
        Credit credit= Crepo.findById(idCredit) .orElse(null) ;
        credit.setState(false);
        Crepo.save(credit);
        return credit;
    }



    @Override
    public Credit retrieveActiveCredit(Integer userid) {

        Credit cr=Crepo.getActiveCreditsByUser(userid) ;
        if(cr==null)
            cr= new Credit(999999, 0, 0);

        return cr;

    }



    @Override
    public Credit retrievelastCredit(Integer userid) {

        Credit cr=Crepo.getlastCreditsByUser(userid) ;
        if(cr==null)
            cr= new Credit(999999, 0, 0);

        return cr;
    }

    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public long countCredits() {
        return creditRepository.countCredits();
    }

    public long countCreditsApproved() {
        return creditRepository.countCreditsApproved();
    }
    public long countCreditsApprovedAndCompleted() {
        return creditRepository.countCreditsApprovedAndCompleted();
    }
    public long countCreditsGoodRisk() {
        return creditRepository.countCreditsBonRisque();
    }
    public long countCreditsNormalRisk() {
        return creditRepository.countCreditsRisqueModere();
    }
    public long countCreditsBadRisk() {
        return creditRepository.countCreditsMauvaisRisque();
    }

    public double getApprovalRate() {
        long totalCredits = this.countCredits();
        long approvedCredits = this.countCreditsApproved();
        if (totalCredits == 0) {
            return 0;
        }
        return (double) approvedCredits / totalCredits * 100.0;
    }
    public double getApprovalAndCompletedRate() {
        long totalCredits = this.countCredits();
        long approvedAndCompletedCredits = this.countCreditsApprovedAndCompleted();
        if (totalCredits == 0) {
            return 0;
        }
        return (double) approvedAndCompletedCredits / totalCredits * 100.0;
    }
    public double getGoodRiskRate() {
        long totalCredits = this.countCredits();
        long GoodRiskCredits = this.countCreditsGoodRisk();
        if (totalCredits == 0) {
            return 0;
        }
        return (double) GoodRiskCredits / totalCredits * 100.0;
    }

    public double getNormalRiskRate() {
        long totalCredits = this.countCredits();
        long NormalRiskCredits = this.countCreditsNormalRisk();
        if (totalCredits == 0) {
            return 0;
        }
        return (double) NormalRiskCredits / totalCredits * 100.0;
    }

    public double getBadRiskRate() {
        long totalCredits = this.countCredits();
        long BadRiskCredits = this.countCreditsBadRisk();
        if (totalCredits == 0) {
            return 0;
        }
        return (double) BadRiskCredits / totalCredits * 100.0;
    }





}
