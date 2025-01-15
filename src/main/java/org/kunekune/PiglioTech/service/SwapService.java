package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Swap;

import java.util.List;

public interface SwapService {
    List<Swap> getSwaps(String userId);
    Swap createSwap(Swap swap);
    void deleteSwap(Long SwapId);
}
