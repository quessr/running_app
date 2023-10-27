package com.example.running_app.data.database.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TB_Run.class}, version = 1)
public abstract class RunDatabase extends RoomDatabase {
    public abstract RunDao runDao();
}
