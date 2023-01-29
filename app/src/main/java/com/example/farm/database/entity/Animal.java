package com.example.farm.database.entity;
import static java.sql.Types.BLOB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="animals")

public class Animal {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String gender;
    private String index_id;



    private byte[] animalPhoto;
   // @ColumnInfo(typeAffinity = BLOB)
    //private String animalPhoto;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndex_id() {
        return index_id;
    }

    public void setIndex_id(String index_id) {
        this.index_id = index_id;
    }
    public byte[] getAnimalPhoto() {
        return animalPhoto;
    }

    public void setAnimalPhoto(byte[] animalPhoto) {
        this.animalPhoto = animalPhoto;
    }


    public Animal(String name, String gender, String index_id, byte[] animalPhoto) {
        this.name = name;
        this.gender = gender;
        this.index_id = index_id;
        this.animalPhoto = animalPhoto;
    }

}
