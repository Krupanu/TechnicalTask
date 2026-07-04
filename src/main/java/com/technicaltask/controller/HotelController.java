package com.technicaltask.controller;

import com.technicaltask.dto.CreateHotelRequest;
import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.dto.SearchCriteria;
import com.technicaltask.model.Hotel;
import com.technicaltask.service.serviceImpl.HotelServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelServiceImpl hotelService;

    @GetMapping("/hotels")
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    public Optional<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelSummaryResponse createHotel(@RequestBody CreateHotelRequest hotel) {
        return hotelService.createHotel(hotel);
    }

    @GetMapping("/search")
    public List<HotelSummaryResponse> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities
    ) {
        return hotelService.search(new SearchCriteria(name, brand, city, country, amenities));
    }
}
