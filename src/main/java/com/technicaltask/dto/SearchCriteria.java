package com.technicaltask.dto;

import java.util.List;

public record SearchCriteria(
        String name,
        String brand,
        String city,
        String country,
        List<String> amenities
) {
}
