package com.example.farm.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.farm.database.FieldDatabase;
import com.example.farm.database.dao.FieldDao;
import com.example.farm.database.entity.Field;

import java.util.List;

public class FieldRepository {
    private final FieldDao fieldDao;
    private final LiveData<List<Field>> fields;

    public FieldRepository(Application application) {
        FieldDatabase database = FieldDatabase.getDatabase(application);
        fieldDao = database.fieldDao();
        fields = fieldDao.findAllField();
    }

    public LiveData<List<Field>> findAllField() {
        return fields;
    }

    public LiveData<List<Field>> findSearchField(String search){return fieldDao.findSearchField(search);}

    public void insertField(Field field) {
        FieldDatabase.databaseWriterExecutor.execute(() -> {
            fieldDao.insertField(field);
        });
    }

    public void updateField(Field field) {
        FieldDatabase.databaseWriterExecutor.execute(() -> {
            fieldDao.updateField(field);
        });
    }

    public void deleteField(Field field) {
        FieldDatabase.databaseWriterExecutor.execute(() -> {
            fieldDao.deleteField(field);
        });
    }
}
