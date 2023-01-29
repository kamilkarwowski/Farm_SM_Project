package com.example.farm.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.farm.database.AnimalDatabase;
import com.example.farm.database.dao.AnimalDao;
import com.example.farm.database.entity.Animal;

import java.util.List;


public class AnimalRepository {
    private final AnimalDao animalDao;
    private final LiveData<List<Animal>> animals;

    public AnimalRepository(Application application) {
        AnimalDatabase database = AnimalDatabase.getDatabase(application);
        animalDao = database.animalDao();
        animals = animalDao.findAll();
    }

    public LiveData<List<Animal>> findAllAnimals() {
        return animals;
    }

    public LiveData<List<Animal>> findSearch(String search){return animalDao.findSearch(search);}

    public void insert(Animal animal) {
        AnimalDatabase.databaseWriterExecutor.execute(() -> {
            animalDao.insert(animal);
        });
    }

    public void update(Animal animal) {
        AnimalDatabase.databaseWriterExecutor.execute(() -> {
            animalDao.update(animal);
        });
    }

    public void delete(Animal animal) {
        AnimalDatabase.databaseWriterExecutor.execute(() -> {
            animalDao.delete(animal);
        });
    }
}
