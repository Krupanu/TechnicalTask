package com.technicaltask.mapper;

import com.technicaltask.dto.AddressResponse;
import com.technicaltask.dto.ArrivalTimeResponse;
import com.technicaltask.dto.ContactsResponse;
import com.technicaltask.dto.CreateHotelRequest;
import com.technicaltask.dto.HotelDetailsResponse;
import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.model.Address;
import com.technicaltask.model.ArrivalTime;
import com.technicaltask.model.Contacts;
import com.technicaltask.model.Hotel;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public Hotel toEntity(CreateHotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.name().trim());
        hotel.setDescription(trimToNull(request.description()));
        hotel.setBrand(request.brand().trim());
        hotel.setAddress(toAddress(request));
        hotel.setContacts(toContacts(request));
        hotel.setArrivalTime(toArrivalTime(request));
        return hotel;
    }

    public HotelSummaryResponse toSummaryResponse(Hotel hotel) {
        return new HotelSummaryResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                formatAddress(hotel.getAddress()),
                hotel.getContacts().getPhone()
        );
    }

    public HotelDetailsResponse toDetailsResponse(Hotel hotel) {
        return new HotelDetailsResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getBrand(),
                new AddressResponse(
                        hotel.getAddress().getHouseNumber(),
                        hotel.getAddress().getStreet(),
                        hotel.getAddress().getCity(),
                        hotel.getAddress().getCountry(),
                        hotel.getAddress().getPostCode()
                ),
                new ContactsResponse(
                        hotel.getContacts().getPhone(),
                        hotel.getContacts().getEmail()
                ),
                new ArrivalTimeResponse(
                        hotel.getArrivalTime().getCheckIn(),
                        hotel.getArrivalTime().getCheckOut()
                ),
                List.copyOf(hotel.getAmenities())
        );
    }

    private Address toAddress(CreateHotelRequest request) {
        Address address = new Address();
        address.setHouseNumber(request.address().houseNumber());
        address.setStreet(request.address().street().trim());
        address.setCity(request.address().city().trim());
        address.setCountry(request.address().country().trim());
        address.setPostCode(request.address().postCode().trim());
        return address;
    }

    private Contacts toContacts(CreateHotelRequest request) {
        Contacts contacts = new Contacts();
        contacts.setPhone(request.contacts().phone().trim());
        contacts.setEmail(request.contacts().email().trim());
        return contacts;
    }

    private ArrivalTime toArrivalTime(CreateHotelRequest request) {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(request.arrivalTime().checkIn().trim());
        arrivalTime.setCheckOut(trimToNull(request.arrivalTime().checkOut()));
        return arrivalTime;
    }

    private String formatAddress(Address address) {
        return "%d %s, %s, %s, %s".formatted(
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
