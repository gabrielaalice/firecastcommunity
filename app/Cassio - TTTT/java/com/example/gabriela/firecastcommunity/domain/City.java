package com.example.gabriela.firecastcommunity.domain;

import com.google.gson.annotations.SerializedName;

public class City  extends EntityModel{

    @SerializedName("nome")
    public String name;

    public City() { }

    public City(int id, String name) {
        setId(id);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && this.getId() != ((City) o).getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString(){
        return name;
    }
}
