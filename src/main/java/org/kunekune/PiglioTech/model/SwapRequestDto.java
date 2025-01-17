package org.kunekune.PiglioTech.model;

import jakarta.validation.constraints.NotBlank;

public record SwapRequestDto(
        @NotBlank(message = "Initiator UID cannot be blank") String initiatorUid,
        @NotBlank(message = "Receiver UID cannot be blank") String receiverUid,
        @NotBlank(message = "Receiver Book ISBN cannot be blank") String receiverIsbn)
{
}
