package com.example.pi_back.Services;

import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.Vote;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VoteService {
    ResponseEntity<String> voteUser(Vote vote);
    ResponseEntity<String> nblikeetdislikeEvent(int IdEvent);
    InternalService mostlikedevent();
    InternalService lesslikedevent();
    List<Object[]> RecommandationEvent();
    Long countTotalLikes();
    Long countTotalDislike();
    Long nbuservote();
    double allUsersVotedAtLeastOnce();
}
