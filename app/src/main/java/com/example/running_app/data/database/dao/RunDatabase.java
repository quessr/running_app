package com.example.running_app.data.database.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TB_Run.class, TB_GPS.class}, version = 1)
public abstract class RunDatabase extends RoomDatabase {
    public abstract RunDao runDao();

    public abstract GpsDao gpsDao();

    public static RunDatabase INSTANCE;
    public static RunDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, RunDatabase.class, "running_db")
                    .fallbackToDestructiveMigration()   //스키마(= database) 버전 변경 가능
//                .addMigrations(RunDatabase.MIGRATION_1_2)
                    .allowMainThreadQueries()   //Main Thread 에서 DB에 IO(입출력) 을 가능하게 함
                    .build();
        }
        return INSTANCE;
    }

}
