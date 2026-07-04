package com.technicaltask;

import com.technicaltask.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();
    }

    @Test
    void shouldCreateHotelAndReturnDetails() throws Exception {
        Long hotelId = createHotel(defaultHotelJson("DoubleTree by Hilton Minsk", "Hilton", "Minsk", "Belarus"));

        mockMvc.perform(get("/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"))
                .andExpect(jsonPath("$.brand").value("Hilton"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.contacts.email").value("hotel@example.com"))
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.amenities", hasSize(0)));
    }

    @Test
    void shouldSearchHotelsByCityAndAmenity() throws Exception {
        Long minskHotelId = createHotel(defaultHotelJson("DoubleTree by Hilton Minsk", "Hilton", "Minsk", "Belarus"));
        createHotel(defaultHotelJson("Warsaw Center Hotel", "Mercure", "Warsaw", "Poland"));
        addAmenities(minskHotelId, "[\"Free WiFi\",\"Fitness center\"]");

        mockMvc.perform(get("/search").param("city", "minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));

        mockMvc.perform(get("/search").param("amenities", "wifi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(minskHotelId));
    }

    @Test
    void shouldReturnHistogramByCityAndAmenities() throws Exception {
        Long firstHotelId = createHotel(defaultHotelJson("DoubleTree by Hilton Minsk", "Hilton", "Minsk", "Belarus"));
        Long secondHotelId = createHotel(defaultHotelJson("Minsk Marriott Hotel", "Marriott", "Minsk", "Belarus"));
        createHotel(defaultHotelJson("Warsaw Center Hotel", "Mercure", "Warsaw", "Poland"));
        addAmenities(firstHotelId, "[\"Free WiFi\",\"Fitness center\"]");
        addAmenities(secondHotelId, "[\"Free WiFi\"]");

        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(2))
                .andExpect(jsonPath("$.Warsaw").value(1));

        mockMvc.perform(get("/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Free WiFi']").value(2))
                .andExpect(jsonPath("$['Fitness center']").value(1));
    }

    @Test
    void shouldReturnValidationErrorForInvalidRequest() throws Exception {
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"));
    }

    @Test
    void shouldReturnNotFoundForUnknownHotel() throws Exception {
        mockMvc.perform(get("/hotels/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    private Long createHotel(String json) throws Exception {
        MvcResult result = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.phone").value("+375 17 309-80-00"))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return Long.valueOf(response.replaceAll(".*\\\"id\\\":(\\d+).*", "$1"));
    }

    private void addAmenities(Long hotelId, String json) throws Exception {
        mockMvc.perform(post("/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    private String defaultHotelJson(String name, String brand, String city, String country) {
        return """
                {
                  "name": "%s",
                  "description": "Hotel description",
                  "brand": "%s",
                  "address": {
                    "houseNumber": 9,
                    "street": "Pobediteley Avenue",
                    "city": "%s",
                    "country": "%s",
                    "postCode": "220004"
                  },
                  "contacts": {
                    "phone": "+375 17 309-80-00",
                    "email": "hotel@example.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """.formatted(name, brand, city, country);
    }
}
