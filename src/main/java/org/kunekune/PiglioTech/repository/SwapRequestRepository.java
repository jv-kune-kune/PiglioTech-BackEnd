package org.kunekune.PiglioTech.repository;

import org.kunekune.PiglioTech.model.SwapRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SwapRequestRepository extends CrudRepository<SwapRequest, Long> {
    @Query("SELECT s FROM SwapRequest s WHERE s.initiator.uid = :uid OR s.receiver.uid = :uid")
    List<SwapRequest> findSwapRequestsByUid(@Param("uid") String uid);
}
