package com.example.gabriela.firecastcommunity.domain;

public class RadioCity extends EntityModel{

    public String urlRadio;
    public City city;

    public RadioCity(){}

    public RadioCity(String urlRadio, City city){
        this.urlRadio = urlRadio;
        this.city = city;
    }
}