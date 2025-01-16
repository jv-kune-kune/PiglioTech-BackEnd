package org.kunekune.PiglioTech.model;

public record MatchDto(Long id, String userOneId, String userTwoId, String userOneIsbn, String userTwoIsbn) {
}
