package org.kunekune.PiglioTech.util;

import org.kunekune.PiglioTech.model.Match;
import org.kunekune.PiglioTech.model.MatchDto;

public class SwapConverters {
  public static MatchDto matchToDto(Match match) {
    return new MatchDto(match.getId(), match.getUserOne(), match.getUserTwo(),
        match.getUserOneBook(), match.getUserTwoBook());
  }
}
