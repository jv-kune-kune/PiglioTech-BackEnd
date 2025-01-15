package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Swap;
import org.kunekune.PiglioTech.repository.SwapRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SwapServiceImpl implements SwapService {

    @Autowired
    private SwapRepository swapRepository;

    @Override
    public List<Swap> getSwaps (String userId) {
        return swapRepository.findAll_ByRequesterUid_and_ResponderUid(userId, userId);
    }

    @Override
    public Swap createSwap(Swap swap) {
        return swapRepository.save(swap);
    }

    @Override
    public void deleteSwap(Long swapId) {
        if (!swapRepository.existsById(swapId)) {
            throw new IllegalArgumentException("Swap with ID " + swapId + " does not exist");
        }
        swapRepository.deleteById(swapId);
        }
    }



