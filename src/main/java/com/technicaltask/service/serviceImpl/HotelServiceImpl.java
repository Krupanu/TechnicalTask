package com.technicaltask.service.serviceImpl;

import com.technicaltask.dto.*;
import com.technicaltask.exception.HotelNotFoundException;
import com.technicaltask.exception.InvalidHistogramParameterException;
import com.technicaltask.mapper.HotelMapper;
import com.technicaltask.model.Hotel;
import com.technicaltask.repository.HotelRepository;
import com.technicaltask.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String parameter) {
        String normalizedParameter = parameter == null ? "" : parameter.toLowerCase(Locale.ROOT);
        List<Hotel> hotels = hotelRepository.findAll();
        Stream<String> values = switch (normalizedParameter) {
            case "brand" -> hotels.stream().map(Hotel::getBrand);
            case "city" -> hotels.stream().map(hotel -> hotel.getAddress().getCity());
            case "country" -> hotels.stream().map(hotel -> hotel.getAddress().getCountry());
            case "amenities" -> hotels.stream().flatMap(hotel -> hotel.getAmenities().stream());
            default -> throw new InvalidHistogramParameterException(parameter);
        };
        return values.filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }
}
