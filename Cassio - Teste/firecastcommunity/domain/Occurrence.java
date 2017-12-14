package com.example.gabriela.firecastcommunity.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bonet on 9/13/16.
 */

public class Occurrence implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public int id;

    @SerializedName("ts")
    public String date;

    @SerializedName("descricao")
    public String description;

    @SerializedName("logradouro")
    public String adressStreet;
    
    @SerializedName("numero")
    public Float addressNumber;

    @SerializedName("complemento")
    public String addressComplement;

    @SerializedName("bairro")
    public String addressNeighborhood;

    @SerializedName("referencia")
    public String addressReferencePoint;

    @SerializedName("cidade")
    public City city;

    @SerializedName("tipoEmergencia")
    public OccurrenceType type;

    @SerializedName("listViatura")
    public List<Car> dispatchedCars;


    @SerializedName("latitude")
    public Double latitude;

    @SerializedName("longitude")
    public Double longitude;

    public Double distance;
}
