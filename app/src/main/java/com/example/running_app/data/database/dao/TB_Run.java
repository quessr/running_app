package com.example.running_app.data.database.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TB_Run {
    @PrimaryKey(autoGenerate = true)
    private int run_id = 0;
    @ColumnInfo(name = "walk_count")
    private int walk_count;
    @ColumnInfo(name = "timer")
    private double timer;
    @ColumnInfo(name = "create_at")
    private String create_at;
    @ColumnInfo(name = "total_distance")
    private double total_distance;
}
