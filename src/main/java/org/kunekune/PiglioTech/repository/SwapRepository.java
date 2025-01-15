package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.Swap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapRepository extends CrudRepository<Swap, Long> {
    List<Swap> findAll_ByRequesterUid_and_ResponderUid(String requesterUid, String responderUid);
}
