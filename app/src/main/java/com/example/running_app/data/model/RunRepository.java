package com.example.running_app.data.model;

import android.app.Application;

import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.List;

public class RunRepository {
    RunDatabase db;

    //생성자 부분 수정
    public RunRepository(Application application) {
        db = RunDatabase.getInstance(application);
    }

    //select 문
    public List<TB_Run> getRunAll() {
        return db.runDao().getRunAll();
    }
    public List<TB_GPS> getGpsAll() {
        return db.gpsDao().getGpsAll();
    }
    public TB_GPS getFirstLocation() {
        return db.gpsDao().getFirstLocation();
    }
    public TB_GPS getLastLocation() {
        return db.gpsDao().getLastLocation();
    }
    public int getLatestRunId() {
        return db.runDao().getLatestRunId();
    }   //Run테이블에서 run_id값 가져오기 위함

    public TB_GPS getMinGpsIdByRunId(int runId) {
        return db.gpsDao().getMinGpsIdByRunId(runId);
    }   //해당 run_id 값에 대해서 gps_id 최대 최소 가져오기 위함

    public TB_GPS getMaxGpsIdByRunId(int runId){
        return  db.gpsDao().getMaxGpsIdByRunId(runId);
    }
    public List<TB_GPS> getAllGpsByRunId(int runId){
        return db.gpsDao().getAllGpsByRunId(runId);
    }

    //insert 문
    public void setInsertRun(TB_Run tbRun) {
        db.runDao().setInsertRun(tbRun);
    }
    public void setInsertGps(TB_GPS tbGps) {
        db.gpsDao().setInsertGps(tbGps);
    }


    //delete 문
    //update 문
}
