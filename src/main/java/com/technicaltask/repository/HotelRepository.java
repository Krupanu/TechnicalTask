package com.technicaltask.repository;

import com.technicaltask.model.Hotel;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;



@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>{
}
