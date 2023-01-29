package com.example.farm.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.farm.database.entity.Field;

import java.util.List;

@Dao
public interface FieldDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertField(Field field);

    @Update
    public void updateField(Field field);

    @Delete
    public void deleteField(Field field);

    @Query("DELETE FROM Field")
    public void deleteAllField();

    @Query("SELECT * FROM Field ORDER BY name")
    public LiveData<List<Field>> findAllField();

    @Query("SELECT * FROM Field WHERE name LIKE :search OR crop LIKE :search")
    public LiveData<List<Field>> findSearchField(String search);
}
