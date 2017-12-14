package com.example.gabriela.firecastcommunity.domain;

import com.google.gson.annotations.SerializedName;

public class OccurrenceType  extends EntityModel{
    
    @SerializedName("nome")
    public String name;

    public OccurrenceType() { }

    public OccurrenceType(int id, String name) {
        setId(id);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && this.getId() != ((OccurrenceType) o).getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
