package com.example.weather.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weather.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity,Integer> {

    LocationEntity findByPincodeAndDate(Integer pincode, LocalDate date);

    LocationEntity findFirstByPincode(Integer pincode);
    
}
