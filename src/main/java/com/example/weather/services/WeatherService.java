package com.example.weather.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.webresources.WarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.weather.dto.LocationDetails;
import com.example.weather.dto.Weather;
import com.example.weather.dto.WeatherInfo;
import com.example.weather.entity.LocationEntity;
import com.example.weather.entity.WeatherEntity;
import com.example.weather.repositoryservice.WeatherRepositoryService;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepositoryService weatherRepositoryService;

    @Autowired
    private RestTemplate restTemplate;

    public Weather sendWeatherInfo(Integer pincode, LocalDate date) {
        //find weather info using pincode and date from DB
        Weather weatherDB = weatherRepositoryService.findPincodeAndDate(pincode, date);

        if (weatherDB != null) {
            return weatherDB;
        }

        // find lat and lon using pincode from DB
        LocationDetails locationDetails = weatherRepositoryService.findPincode(pincode);

        if (locationDetails != null) {
            Double latitude = locationDetails.getLat();
            Double longitude = locationDetails.getLon();

            String url = weatherURL(latitude, longitude);

            //WeatherInfo[] weatherInfoLD = restTemplate.getForObject(url, WeatherInfo[].class);
            Weather weatherLd = restTemplate.getForObject(url, Weather.class);
 
            if (weatherLd == null) {
                return null;
            }

            Integer lid = weatherRepositoryService.findLocationLastLid();
            LocationEntity locationEntity = new LocationEntity(lid, pincode, date, latitude, longitude);
            LocationEntity entity = weatherRepositoryService.saveLocationInfo(locationEntity);

            List<WeatherInfo> weatherInfos = weatherLd.getWeather();
            Integer id = weatherRepositoryService.findWeatherLastId();

            for (WeatherInfo it : weatherInfos) {
                WeatherEntity weatherEntity = new WeatherEntity(id,it.getWid(), it.getMain(), it.getDescription(), it.getIcon(), entity);
                weatherRepositoryService.saveWeatherInfo(weatherEntity);
                id++;
            }

            return weatherLd;
        }

        String locationURL = locationURL(pincode);

        LocationDetails locationDetails2 = null;
        
        try {
            locationDetails2 = restTemplate.getForObject(locationURL, LocationDetails.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
        if (locationDetails2 == null) {
            return null;
        }

        Double lat = locationDetails2.getLat();
        Double lon = locationDetails2.getLon();

        String url = weatherURL(lat, lon);
        //WeatherInfo[] weatherInfo = restTemplate.getForObject(url, WeatherInfo[].class);
        Weather weather = restTemplate.getForObject(url, Weather.class);

        if (weather == null) {
            return null;
        }

        Integer lid = weatherRepositoryService.findLocationLastLid();
        LocationEntity locationEntity = new LocationEntity(lid, pincode, date, lat, lon);
        LocationEntity entity = weatherRepositoryService.saveLocationInfo(locationEntity);

        List<WeatherInfo> weatherInfos = weather.getWeather();
        Integer id = weatherRepositoryService.findWeatherLastId();

        for (WeatherInfo it : weatherInfos) {
            WeatherEntity weatherEntity = new WeatherEntity(id,it.getWid(), it.getMain(), it.getDescription(), it.getIcon(), entity);
            weatherRepositoryService.saveWeatherInfo(weatherEntity);
            id++;
        }
        
        return weather;
    }
    
    private String locationURL(Integer pincode) {
        
        String locationUrl = "http://api.openweathermap.org/geo/1.0/zip?zip="+pincode+
        ",IN&appid=443d31d5782eb33e69db2dfde562dff5";

        return locationUrl;
    }

    private String weatherURL(Double latitude, Double longitude) {

        String url = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+
                    "&lon="+longitude+"&appid=443d31d5782eb33e69db2dfde562dff5";

        return url;
    }

}
