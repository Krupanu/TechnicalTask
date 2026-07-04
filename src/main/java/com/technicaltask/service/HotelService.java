package com.technicaltask.service;

import com.technicaltask.dto.CreateHotelRequest;
import com.technicaltask.dto.HotelDetailsResponse;
import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.dto.SearchCriteria;
import com.technicaltask.model.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    List<HotelSummaryResponse> getAllHotels();
    Optional<Hotel> getHotelById(Long id);
    HotelSummaryResponse createHotel(CreateHotelRequest hotel);
    List<HotelSummaryResponse> search(SearchCriteria criteria);
}
