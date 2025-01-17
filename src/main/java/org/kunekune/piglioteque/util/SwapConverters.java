package org.kunekune.piglioteque.util;

import org.kunekune.piglioteque.model.Match;
import org.kunekune.piglioteque.model.MatchDto;

public class SwapConverters {
    public static MatchDto matchToDto(Match match) {
        return new MatchDto(match.getId(),
                match.getUserOne(),
                match.getUserTwo(),
                match.getUserOneBook(),
                match.getUserTwoBook());
    }
}
