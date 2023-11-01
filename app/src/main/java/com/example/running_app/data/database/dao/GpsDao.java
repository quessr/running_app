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
    @Insert //삽입
    void setInsertGps(TB_GPS tb_gps);

    @Update
        //수정
    void setUpdateGps(TB_GPS tb_gps);

    @Delete
        //삭제
    void setDeleteGps(TB_GPS tb_gps);

    //조회 쿼리
    @Query("SELECT * FROM TB_GPS") //쿼리 : 데이터베이스에 요청하는 명령문
    List<TB_GPS> getGpsAll();

    @Query("SELECT * FROM TB_GPS WHERE run_id = :run_id") //쿼리 : 데이터베이스에 요청하는 명령문
    List<TB_GPS> getGpsAllByRunId(int run_id);
}
