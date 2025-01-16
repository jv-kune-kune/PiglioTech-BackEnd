package org.kunekune.PiglioTech.controller;

import org.kunekune.PiglioTech.model.*;
import org.kunekune.PiglioTech.service.SwapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/swaps")
public class SwapController {

    @Autowired
    private SwapService swapService;

    @GetMapping
    public ResponseEntity<List<MatchDto>> getSwaps(@RequestParam String userId) {
        List<MatchDto> matches = swapService.getMatches(userId);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SwapRequest> createSwap(@RequestBody SwapRequestDto dto) {
        SwapRequest createdSwap = swapService.makeSwapRequest(dto);
        return new ResponseEntity<>(createdSwap, HttpStatus.CREATED);
    }

    @PostMapping("/dismiss")
    public ResponseEntity<Void> dismissSwap(@RequestBody SwapDismissal dismissal) {
        swapService.dismissSwap(dismissal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSwap(@PathVariable Long id) {
        swapService.deleteSwap(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
