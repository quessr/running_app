package com.example.running_app.data.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Data Access Object
@Dao
public interface RunDao {
    @Insert //삽입
    void setInsertRun(TB_Run tb_run);

    @Update //수정
    void setUpdateRun(TB_Run tb_run);

    @Delete //삭제
    void setDeleteRun(TB_Run tb_run);

    //조회 쿼리
    @Query("SELECT * FROM TB_Run") //쿼리 : 데이터베이스에 요청하는 명령문
    LiveData<List<TB_Run>> getRunAll();

    /**
     * 현재 활성화된 달리기 레코드를 반환합니다.
     * run_id를 이용하여 TB_GPS 테이블에 달리기 정보를 채워주세요.
     *
     * @return : 활성화 달리기 정보 (달리기가 완료되지 않는 레코드)
     */
//    @Query("SELECT * FROM TB_Run WHERE is_active == 1 ORDER BY run_id DESC LIMIT 1") //쿼리 : 데이터베이스에 요청하는 명령문
//    List<TB_Run> getLatestActiveOne();

}
