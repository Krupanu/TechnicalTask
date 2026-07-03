package com.technicaltask.service;

import com.technicaltask.dto.HotelSummaryResponse;

import java.util.List;

public interface HotelService {
    List<HotelSummaryResponse> getAllHotels();

}
