package com.technicaltask.dto;

public record HotelSummaryResponse(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}
