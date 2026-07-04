package com.technicaltask.service.serviceImpl;

import com.technicaltask.dto.*;
import com.technicaltask.exception.HotelNotFoundException;
import com.technicaltask.mapper.HotelMapper;
import com.technicaltask.model.Hotel;
import com.technicaltask.repository.HotelRepository;
import com.technicaltask.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelServiceImpl(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    public HotelSummaryResponse createHotel(CreateHotelRequest req) {
        Hotel hotel = hotelMapper.toEntity(req);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toSummaryResponse(savedHotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryResponse> search(SearchCriteria criteria) {
        return hotelRepository.findAll(HotelSpecifications.byFilters(
                        criteria.name(),
                        criteria.brand(),
                        criteria.city(),
                        criteria.country(),
                        criteria.amenities()
                )).stream()
                .map(hotelMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional
    public HotelDetailsResponse addAmenities(Long id, List<String> amenities) {
        Hotel hotel = getHotelEntity(id);
        amenities.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .filter(value -> !hotel.getAmenities().contains(value))
                .forEach(hotel.getAmenities()::add);
        return hotelMapper.toDetailsResponse(hotel);
    }

    private Hotel getHotelEntity(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new HotelNotFoundException(id));
    }
}
