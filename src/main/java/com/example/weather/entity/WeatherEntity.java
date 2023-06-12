package com.example.weather.entity;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weatherinfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherEntity {

    @Id
    private Integer id;

    private Integer wid;
    private String main;
    private String description;
    private String icon; 

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lid", referencedColumnName = "lid")
    private LocationEntity locationEntity;

}
