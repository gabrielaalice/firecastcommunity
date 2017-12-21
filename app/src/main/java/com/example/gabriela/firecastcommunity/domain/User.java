package com.example.gabriela.firecastcommunity.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String email;

    private String password;

    private Integer id_city_occurrence;

    private Integer radiusKilometers;

    private Integer id_city_radio;

    private boolean isNotify;

    private boolean isTimeSilence;

    private Date timeStartSilence;

    private Date timeFinishSilence;

    private List<OccurrenceType> occurrenceTypes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OccurrenceType> getOccurrenceTypes() {
        return occurrenceTypes;
    }

    public void setOccurrenceTypes(List<OccurrenceType> occurrenceTypes) {
        this.occurrenceTypes = occurrenceTypes;
    }

    public Integer getId_city_occurrence() {
        return id_city_occurrence;
    }

    public void setId_city_occurrence(Integer id_city_occurrence) {
        this.id_city_occurrence = id_city_occurrence;
    }

    public Integer getRadiusKilometers() {
        return radiusKilometers;
    }

    public void setRadiusKilometers(Integer radiusKilometers) {
        this.radiusKilometers = radiusKilometers;
    }

    public Integer getId_city_radio() {
        return id_city_radio;
    }

    public void setId_city_radio(Integer id_city_radio) {
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

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    public boolean isTimeSilence() {
        return isTimeSilence;
    }

    public void setTimeSilence(boolean timeSilence) {
        isTimeSilence = timeSilence;
    }

    public Date getTimeStartSilence() {
        return timeStartSilence;
    }

    public void setTimeStartSilence(Date timeStartSilence) {
        this.timeStartSilence = timeStartSilence;
    }

    public Date getTimeFinishSilence() {
        return timeFinishSilence;
    }

    public void setTimeFinishSilence(Date timeFinishSilence) {
        this.timeFinishSilence = timeFinishSilence;
    }
}
