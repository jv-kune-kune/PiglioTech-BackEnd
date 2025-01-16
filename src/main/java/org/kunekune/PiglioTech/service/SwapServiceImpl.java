package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.model.Swap;
import org.kunekune.PiglioTech.repository.SwapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwapServiceImpl implements SwapService {

    private final SwapRepository swapRepository;

    @Autowired
    public SwapServiceImpl(SwapRepository swapRepository) {
        this.swapRepository = swapRepository;
    }


    @Override
    public List<Swap> getSwaps(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return swapRepository.findByRequesterUidAndResponderUid(userId, userId);
    }

    @Override
    public Swap createSwap(Swap swap) {
        if (swap == null || swap.getRequester() == null || swap.getResponder() == null ||
                swap.getRequesterBook() == null || swap.getResponderBook() == null) {
            throw new IllegalArgumentException("Invalid swap data");
        }
        return swapRepository.save(swap);
    }


    @Override
    public void deleteSwap(Long swapId) {
        if (!swapRepository.existsById(swapId)) {
            throw new IllegalArgumentException(String.format("Swap with ID %d does not exist", swapId));
        }
        swapRepository.deleteById(swapId);
    }
}




