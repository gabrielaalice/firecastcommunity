package com.example.gabriela.firecastcommunity.domain;

import java.util.List;

/**
 * Created by bonet on 9/13/16.
 */

public class PreferencesFilterUser {

    public int id;
    public User user;
    public List<OccurrenceType> occurrenceTypes;
    public List<City> citys;
    public Float distanceRadius;
}
