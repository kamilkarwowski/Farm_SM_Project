package com.example.farm.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "field")
public class Field {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private float surface;
    private String crop;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }
    public String getDescription(){return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public Field(String name, float surface, String crop, String description) {
        this.name = name;
        this.surface = surface;
        this.crop = crop;
        this.description = description;
    }
}
