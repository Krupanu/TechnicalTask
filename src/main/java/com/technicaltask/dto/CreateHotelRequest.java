package com.technicaltask.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHotelRequest(
        @NotBlank String name,
        String description,
        @NotBlank String brand,
        @Valid @NotNull AddressRequest address,
        @Valid @NotNull ContactsRequest contacts,
        @Valid @NotNull ArrivalTimeRequest arrivalTime
) {
}
