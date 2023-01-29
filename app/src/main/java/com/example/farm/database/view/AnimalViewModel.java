package com.example.farm.database.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farm.database.entity.Animal;
import com.example.farm.database.repository.AnimalRepository;

import java.util.List;


public class AnimalViewModel extends AndroidViewModel {
    private final AnimalRepository animalRepository;
    private final LiveData<List<Animal>> animals;

    public AnimalViewModel(@NonNull Application application) {
        super(application);
        animalRepository = new AnimalRepository(application);
        animals = animalRepository.findAllAnimals();

    }

    public LiveData<List<Animal>> findAll() {
        return animals;
    }

    public LiveData<List<Animal>> findSearch(String search) {
        return animalRepository.findSearch(search);
    }

    public void insert(Animal animal) {
        animalRepository.insert(animal);
    }

    public void update(Animal animal) {
        animalRepository.update(animal);
    }

    public void delete(Animal animal) {
        animalRepository.delete(animal);
    }
}