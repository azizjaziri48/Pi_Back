package com.example.pi_back.Services;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Entities.Vote;
import com.example.pi_back.Repositories.InternalServiceRepository;
import com.example.pi_back.Repositories.UserRepository;
import com.example.pi_back.Repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {
    private VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final InternalServiceRepository internalServiceRepository;

    @Override
    public ResponseEntity<String> voteUser(Vote vote){
            List<Vote> vote1 = voteRepository.findByIdUser(vote.getIdUser());
            User user = userRepository.findById(vote.getIdUser()).orElse(null);
            InternalService internalService = internalServiceRepository.findById(vote.getIdEvent()).orElse(null);
            if (user == null) {
                return ResponseEntity.ok("User n'existe pas ! ");
            } else {
                if (internalService == null) {
                    return ResponseEntity.ok("event n'existe pas !");
                }
            else{
                for(int i=0;i<vote1.size();i++) {
                    if (vote1.get(i).getIdEvent() == vote.getIdEvent())
                        return ResponseEntity.ok("vous avez deja voté ! ");
                }}}
        voteRepository.save(vote);
        return ResponseEntity.ok("vote enregistré avec succés !");   }

    @Override
    public ResponseEntity<String> nblikeetdislikeEvent(int IdEvent) {
        List<Vote> votes=voteRepository.findAll();
        InternalService internalService=internalServiceRepository.findById(IdEvent).orElse(null);
        int like=0;
        int dislike=0;
        if(internalService == null){
         return   ResponseEntity.ok("Event n'existe pas !");
        }
        else{
        for(int i=0;i< votes.size();i++){
            if(votes.get(i).getIdEvent()==IdEvent){
                if(votes.get(i).getTypeVote().equals("LIKE")){
                    like++;
                }
                else {
                    dislike++;
                }
            }
        }
        return ResponseEntity.ok("Event "+internalService.getName()+"\n\n"+"le nombre de dislike de l'evenement est :"+like+".\n\n"+"le nombre de like de l'evenement est :"+dislike);
    }}

    @Override
    public InternalService mostlikedevent() {
        List<Object[]> result = voteRepository.findMostLikedEvent();
        if (!result.isEmpty()) {
            int eventId = (int) result.get(0)[0];
            return internalServiceRepository.findById(eventId).orElse(null);
        }
        return null;
    }
    @Override
    public InternalService lesslikedevent() {
        List<Object[]> result = voteRepository.findlessLikedEvent();
        if (!result.isEmpty()) {
            int eventId1 = (int) result.get(0)[0];
            return  internalServiceRepository.findById(eventId1).orElse(null);
        }
        return null;
    }

    @Override
    public List<Object[]> RecommandationEvent() {
        List<Object[]> result = voteRepository.RecommandationEvent();

       for(int i=0;i< result.size();i++){
           return Collections.singletonList(result.get(i));
       }
       return null;
    /*    List<Integer> intList = new ArrayList<>();

        for (Object obj : result) {

            intList.add((int)obj);
        }

        List<Integer> ids = result.stream().map(o -> (Integer) o[(Integer)i]).collect(Collectors.toList());
           List<InternalService> events = internalServiceRepository.findAllById(intList);
         return intList;*/
    }
    @Override
    public Long countTotalLikes() {
        return voteRepository.countTotalLikes();
    }

    @Override
    public Long countTotalDislike() {
        return voteRepository.countTotalDisLikes();
    }
    @Override
    public Long nbuservote(){
        return voteRepository.nbuservote();
    }
    @Override
    public double allUsersVotedAtLeastOnce() {
        Long distinctUsers = voteRepository.nbuservote();
        Long totalUsers = userRepository.count();
        double stat = (double) distinctUsers / totalUsers * 100.0;
        return stat;
    }
}