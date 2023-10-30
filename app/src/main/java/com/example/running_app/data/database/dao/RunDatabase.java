package com.example.running_app.data.database.dao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TB_Run.class, TB_GPS.class}, version = 1)
public abstract class RunDatabase extends RoomDatabase {
    public abstract RunDao runDao();

    public abstract GpsDao gpsDao();


}
