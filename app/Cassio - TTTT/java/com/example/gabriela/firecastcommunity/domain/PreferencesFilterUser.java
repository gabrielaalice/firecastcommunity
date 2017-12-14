package com.example.gabriela.firecastcommunity.domain;

import java.util.List;

public class PreferencesFilterUser  extends EntityModel{

    public int id;
    public User user;
    public List<OccurrenceType> occurrenceTypes;
    public List<City> citys;
    public Float distanceRadius;
}
