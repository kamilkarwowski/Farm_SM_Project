package com.example.farm.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.farm.database.dao.FieldDao;
import com.example.farm.database.entity.Field;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Field.class}, version = 1, exportSchema = false)
public abstract class FieldDatabase extends RoomDatabase{

    public abstract FieldDao fieldDao();

    private static volatile FieldDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(() -> {
                FieldDao dao = INSTANCE.fieldDao();
                dao.deleteAllField();

                Field field = new Field("za lasem", 10, "kukurydza", "trza orac pole");
                dao.insertField(field);
            });
        }
    };

    public static FieldDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FieldDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FieldDatabase.class, "field_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
