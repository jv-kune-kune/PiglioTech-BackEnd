package org.kunekune.PiglioTech.controller;

import org.kunekune.PiglioTech.model.Swap;
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
    public ResponseEntity<List<Swap>> getSwaps(@RequestParam String userId) {
        List<Swap> swaps = swapService.getSwaps(userId);
        return new ResponseEntity<>(swaps, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Swap> createSwap(@RequestBody Swap swap) {
        Swap createdSwap = swapService.createSwap(swap);
        return new ResponseEntity<>(createdSwap, HttpStatus.CREATED);
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> deleteSwap(@PathVariable Long id) {
        swapService.deleteSwap(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
