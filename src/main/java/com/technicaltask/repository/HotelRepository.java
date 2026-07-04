package com.technicaltask.repository;

import com.technicaltask.dto.CreateHotelRequest;
import com.technicaltask.dto.HotelSummaryResponse;
import com.technicaltask.model.Hotel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @EntityGraph(attributePaths = {"amenities"})
    Optional<Hotel> findById(Long id);
}
