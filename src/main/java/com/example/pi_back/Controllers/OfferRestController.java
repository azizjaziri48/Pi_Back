package com.example.pi_back.Controllers;


import com.example.pi_back.Entities.Offer;
import com.example.pi_back.Services.EmailSenderService;
import com.example.pi_back.Services.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/offer")
public class OfferRestController {
private OfferService offerService;


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
    Offer updateOffer(@RequestBody Offer offer){
    return offerService.updateOffer(offer);
    }
    @PutMapping("/assignacttointerservice/{idoffer}/{idpartner}")
    public Offer assignPartnerToOffer(@PathVariable("idoffer") Integer idoffer, @PathVariable("idpartner") Integer idpartner){
        return offerService.assignPartnerToOffer(idoffer,idpartner);
    }
}
