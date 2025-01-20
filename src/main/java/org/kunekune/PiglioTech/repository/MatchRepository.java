package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {
  @Query("SELECT m FROM Match m WHERE m.userOne.uid = :uid OR m.userTwo.uid = :uid")
  List<Match> findMatchesByUserUid(@Param("uid") String uid);

  @Query("SELECT m FROM Match m WHERE (m.userOne.uid = :uidOne AND m.userTwo.uid = :uidTwo) OR (m.userOne.uid = :uidTwo AND m.userTwo.uid = :uidOne)")
  List<Match> findMatchesBetweenUsers(@Param("uidOne") String uidOne, @Param("uidTwo") String uidTwo);

}
