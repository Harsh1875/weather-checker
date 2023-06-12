package com.example.weather.entity;

import java.time.LocalDate;
import java.util.List;

import com.example.weather.dto.WeatherInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "locationinfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {
    
    @Id
    private Integer lid;

    @Column(nullable = false)
    private Integer pincode;

    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @Column(name = "latitude", nullable = false)
    private Double lat;
    
    @Column(name = "longitude", nullable = false)
    private Double lon;

}
