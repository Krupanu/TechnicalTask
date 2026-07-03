package com.technicaltask.controller;

import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.service.serviceImpl.HotelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelServiceImpl hotelService;

    @GetMapping("/hotels")
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }
}
