package com.example.gabriela.firecastcommunity.domain;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String email;

    private String password;

    private int id_city_occurrence;

    private int radiusKilometers;

    private int id_city_radio;

    private List<OccurrenceType> occurrenceTypes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OccurrenceType> getOccurrenceTypes() {
        return occurrenceTypes;
    }

    public void setOccurrenceTypes(List<OccurrenceType> occurrenceTypes) {
        this.occurrenceTypes = occurrenceTypes;
    }

    public int getId_city_occurrence() {
        return id_city_occurrence;
    }

    public void setId_city_occurrence(int id_city_occurrence) {
        this.id_city_occurrence = id_city_occurrence;
    }

    public int getRadiusKilometers() {
        return radiusKilometers;
    }

    public void setRadiusKilometers(int radiusKilometers) {
        this.radiusKilometers = radiusKilometers;
    }

    public int getId_city_radio() {
        return id_city_radio;
    }

    public void setId_city_radio(int id_city_radio) {
        this.id_city_radio = id_city_radio;
    }

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
}
