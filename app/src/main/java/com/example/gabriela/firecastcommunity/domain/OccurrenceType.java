package com.example.gabriela.firecastcommunity.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bonet on 9/21/16.
 */

public class OccurrenceType implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int id;
    
    @SerializedName("nome")
    public final String name;

    public OccurrenceType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) && this.id != ((OccurrenceType) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
