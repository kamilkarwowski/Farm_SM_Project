package com.example.farm.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.farm.database.dao.AnimalDao;
import com.example.farm.database.entity.Animal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Database(entities = {Animal.class}, version = 1, exportSchema = false)
public abstract class AnimalDatabase extends RoomDatabase {

    public abstract AnimalDao animalDao();

    private static volatile AnimalDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                AnimalDao dao = INSTANCE.animalDao();
                dao.deleteAll();

               // Animal animal = new Animal("Pimpek", "female", "PL005278345672");
              //  dao.insert(animal);
            });
        }
    };

    public static AnimalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AnimalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AnimalDatabase.class, "animal_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}