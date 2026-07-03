package com.technicaltask.dto;

import com.technicaltask.model.Hotel;
import lombok.Data;

public record HotelDto(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}

