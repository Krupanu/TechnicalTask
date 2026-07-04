package com.technicaltask.dto;

import jakarta.validation.constraints.NotBlank;

public record ArrivalTimeRequest(
        @NotBlank String checkIn,
        String checkOut
) {
}
