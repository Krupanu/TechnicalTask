package com.technicaltask.controller;

import com.technicaltask.dto.CreateHotelRequest;
import com.technicaltask.dto.HotelDetailsResponse;
import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.dto.SearchCriteria;
import com.technicaltask.exception.HotelNotFoundException;
import com.technicaltask.mapper.HotelMapper;
import com.technicaltask.model.Hotel;
import com.technicaltask.service.serviceImpl.HotelServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelServiceImpl hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/hotels")
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    public HotelDetailsResponse getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(hotelMapper::toDetailsResponse)
                .orElseThrow(() -> new HotelNotFoundException(id));
    }

    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelSummaryResponse createHotel(@RequestBody @Valid CreateHotelRequest hotel) {
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

    @PostMapping("/hotels/{id}/amenities")
    public HotelDetailsResponse addAmenities(
            @PathVariable Long id,
            @Valid @RequestBody @NotEmpty List<@NotBlank String> amenities
    ) {
        return hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
