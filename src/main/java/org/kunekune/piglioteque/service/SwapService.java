package org.kunekune.piglioteque.service;

import org.kunekune.piglioteque.model.*;

import java.util.List;

public interface SwapService {
    List<MatchDto> getMatches(String userId);
    SwapRequest makeSwapRequest(SwapRequestDto dto);
    void dismissSwap(SwapDismissal dismissal);
}
