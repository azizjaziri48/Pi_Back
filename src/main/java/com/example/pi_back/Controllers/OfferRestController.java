package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Statistic;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.UserRepository;
import com.example.pi_back.Services.EmailSenderService;
import com.example.pi_back.Services.OfferService;
import com.example.pi_back.Services.StatisticService;


import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/offer")
public class OfferRestController {

    private OfferService offerService;
    private StatisticService statisticService;
    private final OfferRepository offerRepository;
   private UserRepository userRepository;
    @Autowired
    private EmailSenderService service;

    @PostMapping("/add")
    Offer AddOffer(@RequestBody Offer offer) throws IOException, MessagingException {
        File file = ResourceUtils.getFile("src/main/java/com/example/pi_back/addoffer.html");
        System.out.println("File Found : " + file.exists());
        String content = new String(Files.readAllBytes(file.toPath()));
        content = content.replace("${offer}", offer.getName());
        content = content.replace("${partner}", offer.getValeur().toString());
        service.sendSimpleEmail("mohamedaziz.jaziri1@esprit.tn",content,"new Offer !");
        return offerService.AddOffer(offer);
    }

    @GetMapping("/all")
    List<Offer> retrieveAllOffers() {
        return offerService.retrieveAllOffers();
    }

    @DeleteMapping("/delete/{id}")
    void removeOffer(@PathVariable("id") Integer idOffer) {
        offerService.removeOffer(idOffer);
    }

    @GetMapping("/get/{id}")
    public Offer retrieveOffer(@PathVariable("id") int idOffer) {
        return offerService.retrieveOffer(idOffer);
    }

    @PutMapping("/update")
    Offer updateOffer(@RequestBody Offer offer) {
        return offerService.updateOffer(offer);
    }

    @PutMapping("/assignoffertopartner/{idoffer}/{idpartner}")
    public Offer assignPartnerToOffer(@PathVariable("idoffer") Integer idoffer, @PathVariable("idpartner") Integer idpartner) {
        return offerService.assignPartnerToOffer(idoffer, idpartner);
    }

    @GetMapping("/getoffreselonpartenaire/{nom}")
    @Transactional
    List<Offer> findByPartner_Name(@PathVariable("nom") String name) {
        return offerService.findByPartner_Name(name);
    }

    @GetMapping("/statisticScore")
    @ResponseBody
    public ResponseEntity<String> getStatistic() {
        List<Offer> sc = offerService.retrieveAllOffers();
        Statistic statistics = statisticService.CreateStatistic(sc);
        //l'offre ayant la valeur la plus élevée
        Double max=statistics.getMax().doubleValue();
        Offer offer=offerRepository.findOfferByValeur(max);
        String texte1="La valeur maximale des offres :"+max+"\n\n"+offer;
        //l'offre ayant la valeur la plus faible
        Double min=statistics.getMin().doubleValue();
        Offer offer1=offerRepository.findOfferByValeur(min);
        String texte2="La valeur minimale des offres:"+min+"\n\n"+offer1;
        //les info en plus
        BigDecimal sum=statistics.getSum();
        BigDecimal avg=statistics.getAvg();
        long count=statistics.getCount();
        String texte3="la somme des valeurs des offres:"+sum+"\n\n"+"la moyenne des valeurs des offres:"+avg+"\n\n"+"le nombre de valeurs des offres:"+count;
        return ResponseEntity.ok(texte1+"\n\n"+texte2+"\n\n"+texte3);
    }
    @GetMapping("/historiquedesoffres/{iduser}")
    public ResponseEntity<List<Offer>>  historiquedesoffres(@PathVariable("iduser") int userid){
        User user= userRepository.findById(userid).orElse(null);
        List<Offer> offers= offerService.historiqueOffers(userid);
        String Text1="l'historique des offres de l'utilisateur "+ user.getFirstname()+"-"+user.getSecondname();
        return  new ResponseEntity<List<Offer>>(offers, HttpStatus.OK);
    }
    @GetMapping("/VAN/{idoffer}/{discountrate}/{duration}")
    public double calculateNetPresentValue(@PathVariable("idoffer") int id,@PathVariable("discountrate") double discountRate,@PathVariable("duration") int duration) {
        double netPresentValue = 0;
        double discountedCashFlow = 0;

        Offer offer=offerRepository.findById(id).orElse(null);
        for (int i = 1; i <= duration; i++) {
            discountedCashFlow =offer.getValeur() / Math.pow(1 + discountRate, i);
            netPresentValue += discountedCashFlow;
        }
        return netPresentValue;
    }
    @GetMapping("/interestamount/{id}/{duration}/{interestrate}")
    public double calculateInterest(@PathVariable("id") int idoffer,@PathVariable("duration") int duration,@PathVariable("interestrate") double interestrate) {
        Offer offer=offerRepository.findById(idoffer).orElse(null);
        double interests = offer.getValeur() * duration * interestrate / 100;
        return interests;
    }
}
