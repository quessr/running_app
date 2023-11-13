package com.example.running_app.data.database.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("run_id")},
        tableName = "TB_GPS",
        foreignKeys = @ForeignKey(entity = TB_Run.class,
                parentColumns = "run_id",
                childColumns = "run_id",
                onDelete = ForeignKey.CASCADE))
//@Entity(tableName = "TB_GPS")
public class TB_GPS {
    @PrimaryKey(autoGenerate = true)
    private int gps_id;
    @ColumnInfo(name = "run_id")
    private int run_id;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lon")
    private double lon;
//    @ColumnInfo(name = "create_at")
//    private String create_at;

    public int getGps_id() {
        return gps_id;
    }

    public void setGps_id(int gps_id) {
        this.gps_id = gps_id;
    }

    public int getRun_id() {
        return run_id;
    }

    public void setRun_id(int run_id) {
        this.run_id = run_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

//    public String getCreate_at() {
//        return create_at;
//    }
//
//    public void setCreate_at(String create_at) {
//        this.create_at = create_at;
//    }
}
