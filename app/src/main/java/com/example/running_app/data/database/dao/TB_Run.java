package com.example.running_app.data.database.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TB_Run")
public class TB_Run {
    @PrimaryKey(autoGenerate = true)
    private int run_id;
    @ColumnInfo(name = "walk_count")
    private int walk_count;
    @ColumnInfo(name = "timer")
    private String timer;
    @ColumnInfo(name = "create_at")
    private String create_at;

    // 1: active (달리기 중)
    // 0 : not active (달리기 완료)
//    @ColumnInfo(name = "is_active")
//    private int is_active;

//    public int getIs_active() {
//        return is_active;
//    }
//
//    public void setIs_active(int is_active) {
//        this.is_active = is_active;
//    }

    public int getRun_id() {
        return run_id;
    }

    public void setRun_id(int run_id) {
        this.run_id = run_id;
    }

    public int getWalk_count() {
        return walk_count;
    }

    public void setWalk_count(int walk_count) {
        this.walk_count = walk_count;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "TB_Run{" +
                "run_id=" + run_id +
                ", walk_count=" + walk_count +
                ", timer=" + timer +
                ", create_at='" + create_at + '\'' +
                '}';
    }
}
