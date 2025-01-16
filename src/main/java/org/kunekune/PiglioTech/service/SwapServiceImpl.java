package org.kunekune.PiglioTech.service;

import jakarta.persistence.EntityExistsException;
import org.kunekune.PiglioTech.model.*;
import org.kunekune.PiglioTech.repository.MatchRepository;
import org.kunekune.PiglioTech.repository.SwapRepository;
import org.kunekune.PiglioTech.repository.SwapRequestRepository;
import org.kunekune.PiglioTech.repository.UserRepository;
import org.kunekune.PiglioTech.util.SwapConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwapServiceImpl implements SwapService {

    private final SwapRepository swapRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Override
    public List<MatchDto> getMatches(String userId) {
        return matchRepository.findMatchesByUserUid(userId)
                .stream()
                .filter(m -> {
                    if (m.getUserOne().getUid().equals(userId)) {
                        return !m.isUserOneDismissed();
                    } else {
                        return !m.isUserTwoDismissed();
                    }
                })
                .map(SwapConverters::matchToDto)
                .toList();
    }

    @Override
    public SwapRequest makeSwapRequest(SwapRequestDto dto) {
        List<SwapRequest> existingSwaps = swapRequestRepository.findSwapRequestsByUid(dto.initiatorUid());
        for (SwapRequest swap : existingSwaps) {
            if (swap.getInitiator().getUid().equals(dto.initiatorUid())
            && swap.getReceiver().getUid().equals(dto.receiverUid())
            && swap.getReceiverBook().getIsbn().equals(dto.receiverIsbn())) {
                throw new EntityExistsException("Identical swap request has already been made");
            }

            if (swap.getInitiator().getUid().equals(dto.receiverUid())
            && swap.getReceiver().getUid().equals(dto.initiatorUid())) {
                Match match = new Match(swap.getReceiver(), swap.getInitiator(),
                        swap.getReceiverBook(),
                        swap.getInitiator().getBookByIsbn(dto.receiverIsbn()));
                matchRepository.save(match);
            }
        }

        User initiator = userRepository.findById(dto.initiatorUid()).orElseThrow();
        User receiver = userRepository.findById(dto.receiverUid()).orElseThrow();
        Book receieverBook = receiver.getBookByIsbn(dto.receiverIsbn());

        SwapRequest request = new SwapRequest(initiator, receiver, receieverBook);

        return swapRequestRepository.save(request);
    }

    @Override
    public void dismissSwap(SwapDismissal dismissal) {
        // Dummy
    }
}




