package com.example.pi_back.Repositories;
import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.Partner;
import com.example.pi_back.Entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByIdUser(int id);
    @Query("SELECT v.idEvent, COUNT(v.id) as totalLikes FROM Vote v WHERE v.typeVote = 'LIKE' " +
            "GROUP BY v.idEvent " +
            "ORDER BY totalLikes DESC")
    List<Object[]> findMostLikedEvent();
    @Query("SELECT v.idEvent, COUNT(v.id) as totalDislikes FROM Vote v WHERE v.typeVote = 'DISLIKE' " +
            "GROUP BY v.idEvent " +
            "ORDER BY totalDislikes DESC")
    List<Object[]> findlessLikedEvent();
    @Query("SELECT v.idEvent, COUNT(v.id) as totalLikes FROM Vote v WHERE v.typeVote = 'LIKE' " +
            "GROUP BY v.idEvent " +
            "ORDER BY totalLikes DESC")
    List<Object[]> RecommandationEvent();
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.typeVote = 'LIKE'")
    Long countTotalLikes();
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.typeVote = 'DISLIKE'")
    Long countTotalDisLikes();
    @Query("SELECT COUNT(DISTINCT v.idUser) FROM Vote v")
    Long nbuservote();


}
