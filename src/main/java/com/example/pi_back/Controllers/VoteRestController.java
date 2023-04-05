package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.Vote;
import com.example.pi_back.Services.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/Voter")
public class VoteRestController {
    private VoteService voteService;
    @PostMapping("/vote")
    ResponseEntity<String> voter(@RequestBody Vote vote){
            return voteService.voteUser(vote);
    }
    @GetMapping("/nbvote/{idevent}")
    ResponseEntity<String> nbvote(@PathVariable("idevent") int ide){
        return voteService.nblikeetdislikeEvent(ide);
    }
    @GetMapping("/mostandlesslikedevent")
    ResponseEntity<String> mostliked(){
      InternalService internalService = voteService.mostlikedevent();
      InternalService internalService1 = voteService.lesslikedevent();
      long Nblike=voteService.countTotalLikes();
      long Nbdislike=voteService.countTotalDislike();
      long nbuservote=voteService.nbuservote();
      double stat=voteService.allUsersVotedAtLeastOnce();
    return ResponseEntity.ok("event le plus liké et apprécié par les clients :"+internalService.getName()+"\n\n"+"event le plus disliké et le moin apprécié par les clients:"+internalService1.getName()
    +"\n\n"+"le nombre total de like : "+Nblike+"\n\n"+"le nombre total de dislike : "+Nbdislike+"\n\n"+
            "le nombre de user ayant voté est : "+nbuservote+"\n\n"+stat+" % des users ont votés au moin a un evenement.");
    }
    @GetMapping("/Recommandationevent")
    List<Object[]> Recommandation(){
        return voteService.RecommandationEvent();
    }


}
