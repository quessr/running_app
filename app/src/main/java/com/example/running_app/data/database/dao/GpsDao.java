package com.example.running_app.data.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Data Access Object
@Dao
public interface GpsDao {
    @Insert
        //삽입
    void setInsertGps(TB_GPS tb_gps);

    @Update
        //수정
    void setUpdateGps(TB_GPS tb_gps);

    @Delete
        //삭제
    void setDeleteGps(TB_GPS tb_gps);

    //조회 쿼리
    @Query("SELECT * FROM TB_GPS")
    //쿼리 : 데이터베이스에 요청하는 명령문
    List<TB_GPS> getGpsAll();

    //처음 좌표 데이터 조회
    @Query("SELECT * FROM TB_GPS WHERE gps_id = (SELECT MIN(gps_id) FROM TB_GPS)")
    TB_GPS getFirstLocation();

    //마지막 좌표 데이터 조회
    @Query("SELECT * FROM TB_GPS WHERE gps_id = (SELECT MAX(gps_id) FROM TB_GPS)")
    TB_GPS getLastLocation();


    //    @Query("SELECT * FROM TB_GPS WHERE run_id = :run_id AND gps_id = (SELECT MIN(gps_id) FROM TB_GPS)")
//    TB_GPS getMinGpsIdByRunId(int runId);

    //처음 좌표 데이터 조회
    @Query("SELECT * FROM TB_GPS INNER JOIN TB_Run ON TB_GPS.run_id = TB_Run.run_id WHERE TB_GPS.run_id = :runId ORDER BY TB_GPS.gps_id ASC LIMIT 1")
    TB_GPS getMinGpsIdByRunId(int runId);

    //마지막 좌표 데이터 조회
    @Query("SELECT * FROM TB_GPS INNER JOIN TB_Run ON TB_GPS.run_id = TB_Run.run_id WHERE TB_GPS.run_id = :runId ORDER BY TB_GPS.gps_id DESC LIMIT 1")
    TB_GPS getMaxGpsIdByRunId(int runId);


}
