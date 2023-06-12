package com.example.weather.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.example.weather.WeatherApplication;
import com.example.weather.dto.LocationDetails;
import com.example.weather.dto.Weather;
import com.example.weather.dto.WeatherInfo;
import com.example.weather.repositoryservice.WeatherRepositoryService;
import com.example.weather.services.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = {WeatherApplication.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class WeatherServiceTest {
    
    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherRepositoryService weatherRepositoryServiceMock;

    @Mock
    private RestTemplate restTemplateMock;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        objectMapper = new ObjectMapper();
    }

    @Test
    public void getWeatherfromDB() {

        WeatherInfo weatherInfo = new WeatherInfo(801, "Cloud", "Very clouding", "202");
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        weatherInfos.add(weatherInfo);

        Weather weatherexpected = new Weather(weatherInfos);

        when(weatherRepositoryServiceMock.findPincodeAndDate(anyInt(), any(LocalDate.class))).thenReturn(weatherexpected);

        Weather weather = weatherService.sendWeatherInfo(411014, LocalDate.now());

        verify(weatherRepositoryServiceMock, times(1)).findPincodeAndDate(anyInt(), any(LocalDate.class));

        assertEquals(weatherexpected, weather);
    }

    @Test
    public void getWeatherwithLocationFromDB() {

        WeatherInfo weatherInfo = new WeatherInfo(801, "Cloud", "Very clouding", "202");
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        weatherInfos.add(weatherInfo);

        Weather weatherexpected = new Weather(weatherInfos);
        LocationDetails locationDetails = new LocationDetails(20.012, 72.194);


        when(weatherRepositoryServiceMock.findPincode(anyInt())).thenReturn(locationDetails);
        Mockito.doReturn(weatherexpected).when(restTemplateMock).getForObject(anyString(), eq(Weather.class));

        Weather weather = weatherService.sendWeatherInfo(411014, LocalDate.now());

        verify(weatherRepositoryServiceMock, times(1)).findPincode(anyInt());

        assertEquals(weatherexpected, weather);
    }


    @Test
    public void getWeatherInfoFromAPI() {

        LocationDetails locationDetails = new LocationDetails(20.012, 72.194);
        Mockito.doReturn(locationDetails).when(restTemplateMock).getForObject(anyString(), eq(LocationDetails.class));

        WeatherInfo weatherInfo = new WeatherInfo(801, "Cloud", "Very clouding", "202");
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        weatherInfos.add(weatherInfo);

        Weather weatherexpected = new Weather(weatherInfos);

        Mockito.doReturn(weatherexpected).when(restTemplateMock).getForObject(anyString(), eq(Weather.class));

        Weather weather = weatherService.sendWeatherInfo(411014, LocalDate.now());

        assertEquals(weatherexpected, weather);
    }

}
