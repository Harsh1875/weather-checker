package com.example.weather.repository;

import java.util.List;

import com.example.weather.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.weather.entity.WeatherEntity;

public interface WeatherRepostory extends JpaRepository<WeatherEntity, Integer>{

    List<WeatherEntity> findByLocationEntity(LocationEntity locationEntity);
    
}
