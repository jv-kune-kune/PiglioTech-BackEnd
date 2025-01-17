package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE m.userOne.uid = :uid OR m.userTwo.uid = :uid")
    List<Match> findMatchesByUserUid(@Param("uid") String uid);
}
