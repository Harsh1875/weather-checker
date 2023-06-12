package com.example.weather.repositoryservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.weather.dto.LocationDetails;
import com.example.weather.dto.Weather;
import com.example.weather.dto.WeatherInfo;
import com.example.weather.entity.LocationEntity;
import com.example.weather.entity.WeatherEntity;
import com.example.weather.repository.LocationRepository;
import com.example.weather.repository.WeatherRepostory;

@Service
public class WeatherRepositoryService {
    
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private WeatherRepostory weatherRepostory;

    @Autowired
    private ModelMapper modelMapper;

    public Weather findPincodeAndDate(Integer pincode, LocalDate date) {
        LocationEntity locationEntity =  locationRepository.findByPincodeAndDate(pincode, date);
        if (locationEntity == null) {
            return null;
        }

        List<WeatherEntity> weatherEntity = weatherRepostory.findByLocationEntity(locationEntity);

        List<WeatherInfo> weatherInfos = new ArrayList<>();

        for (WeatherEntity entity : weatherEntity) {
            weatherInfos.add(modelMapper.map(entity, WeatherInfo.class));
        }

        return new Weather(weatherInfos);
    }

    public LocationDetails findPincode(Integer pincode) {
        LocationEntity locationEntity = locationRepository.findFirstByPincode(pincode);
        if (locationEntity == null) {
            return null;
        }

        LocationDetails locationDetails = modelMapper.map(locationEntity, LocationDetails.class);
        return locationDetails;
    }

    public void saveWeatherInfo(WeatherEntity weatherEntity) {
        weatherRepostory.save(weatherEntity);
    }

    public LocationEntity saveLocationInfo(LocationEntity locationEntity) {
        return locationRepository.save(locationEntity);
    }

    public Integer findLocationLastLid() {
        List<LocationEntity> locationEntities = locationRepository.findAll();
        Integer id = 0;

        if (locationEntities.size() == 0) {
            return id+1;
        }

        for (int i=locationEntities.size()-1; i > locationEntities.size()-2; i--) {
            id = locationEntities.get(i).getLid() + 1;
        }

        return id;
    }

    public Integer findWeatherLastId() {
        List<WeatherEntity> weatherEntities = weatherRepostory.findAll();
        Integer id = 0;

        if (weatherEntities.size() == 0) {
            return id+1;
        }

        for (int i=weatherEntities.size()-1; i > weatherEntities.size()-2; i--) {
            id = weatherEntities.get(i).getId() + 1;
        }

        return id;
    }
    


}
