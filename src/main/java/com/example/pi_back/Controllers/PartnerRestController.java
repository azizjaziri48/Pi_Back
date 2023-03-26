package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Services.PartnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/partner")
public class PartnerRestController {
    private PartnerService partnerService;
    @GetMapping("/all")
    List<Partner> retrieveAllPartners() {
        return partnerService.retrieveAllPartners();
    }
    @PostMapping("/add")
    Partner AddPartner (@RequestBody Partner partner){
    return partnerService.AddPartner(partner);
    }
    @DeleteMapping("/delete/{id}")
    void removePartner (@PathVariable("id") Integer idPartner){
    partnerService.removePartner(idPartner);
    }
    @GetMapping("/get/{id}")
    Partner retrievePartner (@PathVariable("id") Integer idPartner){
    return partnerService.retrievePartner(idPartner);
    }
    @PutMapping("/update")
    Partner updatePartner(@RequestBody Partner partner){
        return partnerService.updatePartner(partner);
    }
    @GetMapping("/partnerinfo")
    public ResponseEntity<String> mostfrequent()  {
        // créer l'objet
        Partner most = partnerService.findMostFrequentPartner();
        Partner less =partnerService.findLessFrequentPartner();
        List<Partner> partnerListwithoutoffer = partnerService.findPartnersWithoutOffers();
        Long nbpartnerwithoffer = partnerService.countPartnersWithOffers();
        double pourcentage=partnerService.getPartnersOfferPercentages();
        double pourcentage1=partnerService.getPartnersOfferPercentages1();
        // créer le texte
        String texte = "le partner le plus fréquent est ";
        String texte1 ="le partner le moin fréquent est ";
        String texte2 = "les partners qui ont participer a aucunes offers :";
        String texte3="le nombre de partener ayant participer aux offres :";
        String texte4="selon les statistiques, nous pouvons déduire que : ";
        String texte5="% des partners n'ont participer a aucune offres";
        String texte6="% des partners ont participer aux offres";
        // créer la réponse avec l'objet et le texte
        ResponseEntity<String> response = ResponseEntity.ok()
                .header("Custom-Header", "valeur-personnalisee")
                .body(texte3 + "-" + nbpartnerwithoffer.toString() + "\n\n" + texte + "-" + most.toString() + "\n\n" + texte1  + "-" + less.toString() + "\n\n" + texte2 + "-" +partnerListwithoutoffer.toString() + "\n\n" + texte4+"\n\n"+ pourcentage +texte5+"\n\n"+pourcentage1+texte6);

        return response;
    }
@GetMapping("/nb")
    public Long countpartnerwithoffer(){
        return partnerService.countPartnersWithOffers();
}
}
