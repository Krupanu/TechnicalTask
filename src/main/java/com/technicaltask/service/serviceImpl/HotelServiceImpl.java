package com.technicaltask.service.serviceImpl;

import com.technicaltask.dto.HotelDto;
import com.technicaltask.model.Hotel;
import com.technicaltask.repository.HotelRepository;
import com.technicaltask.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }


    @Override
    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll();

    }
}
