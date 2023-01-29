package com.example.farm.database.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.farm.database.entity.Field;
import com.example.farm.database.repository.FieldRepository;

import java.util.List;

public class FieldViewModel extends AndroidViewModel {
    private final FieldRepository fieldRepository;
    private final LiveData<List<Field>> fields;

    public FieldViewModel(@NonNull Application application) {
        super(application);
        fieldRepository = new FieldRepository(application);
        fields = fieldRepository.findAllField();

    }

    public LiveData<List<Field>> findAllField() {
        return fields;
    }


    public LiveData<List<Field>> findSearchField(String search) {
        return fieldRepository.findSearchField(search);
    }

    public void insertField(Field field) {
        fieldRepository.insertField(field);
    }

    public void updateField(Field field) {
        fieldRepository.updateField(field);
    }

    public void deleteField(Field field) {
        fieldRepository.deleteField(field);
    }
}
