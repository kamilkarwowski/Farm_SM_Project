package com.example.farm.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import  com.example.farm.database.entity.Animal;


@Dao
public interface AnimalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Animal animal);

    @Update
    public void update(Animal animal);

    @Delete
    public void delete(Animal animal);

    @Query("DELETE FROM animals")
    public void deleteAll();

    @Query("SELECT * FROM animals ORDER BY name")
    public LiveData<List<Animal>> findAll();

    @Query("SELECT * FROM animals WHERE name LIKE :search OR index_id LIKE :search")
    public LiveData<List<Animal>> findSearch(String search);


}
