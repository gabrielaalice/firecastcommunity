package com.example.gabriela.firecastcommunity.domain;

import java.util.List;

public class User extends EntityModel{

    public User(){}
    private String name;
    private String email;
    private String password;
    private int radiusKilometers;
    private City city_occurrence;
    private RadioCity radio_city;
    private List<OccurrenceType> occurrenceTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRadiusKilometers() {
        return radiusKilometers;
    }

    public void setRadiusKilometers(int radiusKilometers) {
        this.radiusKilometers = radiusKilometers;
    }

    public City getCity_occurrence() {
        return city_occurrence;
    }

    public void setCity_occurrence(City city_occurrence) {
        this.city_occurrence = city_occurrence;
    }

    public RadioCity getRadio_city() {
        return radio_city;
    }

    public void setRadio_city(RadioCity radio_city) {
        this.radio_city = radio_city;
    }

    public List<OccurrenceType> getOccurrenceTypes() {
        return occurrenceTypes;
    }

    public void setOccurrenceTypes(List<OccurrenceType> occurrenceTypes) {
        this.occurrenceTypes = occurrenceTypes;
    }
}
