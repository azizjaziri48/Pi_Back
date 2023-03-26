package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Entities.Statistic;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Repositories.OfferRepository;
import com.example.pi_back.Repositories.UserRepository;
import com.example.pi_back.Services.OfferService;
import com.example.pi_back.Services.StatisticService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.transaction.Transactional;
import java.math.BigDecimal;
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

   /* @GetMapping("/statt")
    public ModelAndView Statpage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("Stat");
        return model;
    }*/

    @PostMapping("/add")
    Offer AddOffer(@RequestBody Offer offer) {
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
        BigDecimal max=statistics.getMax();
        Offer offer=offerRepository.findOfferByValeur(max.doubleValue());
        String texte1="La valeur maximale des offres :"+max+"\n\n"+offer;
        //l'offre ayant la valeur la plus faible
        BigDecimal min=statistics.getMin();
        Offer offer1=offerRepository.findOfferByValeur(min.doubleValue());
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
}
