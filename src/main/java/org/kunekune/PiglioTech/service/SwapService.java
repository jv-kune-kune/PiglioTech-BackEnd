package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.*;

import java.util.List;

public interface SwapService {
  List<MatchDto> getMatches(String userId);

  SwapRequest makeSwapRequest(SwapRequestDto dto);

  void dismissSwap(SwapDismissal dismissal);
}
