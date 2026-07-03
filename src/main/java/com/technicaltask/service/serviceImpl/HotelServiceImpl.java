package com.technicaltask.service.serviceImpl;

import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.mapper.HotelMapper;
import com.technicaltask.model.Hotel;
import com.technicaltask.repository.HotelRepository;
import com.technicaltask.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}
