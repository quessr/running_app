package com.example.running_app.data.model;

import android.app.Application;

import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.List;

public class RunRepository {
    RunDatabase db;
    // LiveData<List<TB_Run>> getRunAll;

    // LiveData<List<TB_GPS>> getGpsAll;

    // TB_GPS getFirstLocation;
    // TB_GPS getLastLocation;
    // List<TB_Run> getLatestActiveOne;

    public RunRepository(Application application){
        db = RunDatabase.getInstance(application);
//        getRunAll = runDatabase.runDao().getRunAll();
//        getGpsAll = runDatabase.gpsDao().getGpsAll();
//
//        getFirstLocation = runDatabase.gpsDao().getFirstLocation();
//        getLastLocation = runDatabase.gpsDao().getLastLocation();
//        getLatestActiveOne = runDatabase.runDao().getLatestActiveOne();
    }

   public List<TB_Run> getRunAll(){
        return db.runDao().getRunAll();
    }
    public List<TB_GPS> getGpsAll(){
        return db.gpsDao().getGpsAll();
    }

    public TB_GPS getFirstLocation(){
        return db.gpsDao().getFirstLocation();
    }

    public TB_GPS getLastLocation(){
        return db.gpsDao().getLastLocation();
    }

//    public List<TB_Run> getLatestActiveOne() {
//        return runDatabase.runDao().getLatestActiveOne();
//    }

    //insert 문
    public void setInsertRun(TB_Run tbRun) {
        db.runDao().setInsertRun(tbRun);
    }

    public void setInsertGps(TB_GPS tbGps) {
//        try {
//            Thread thread = new Thread(new Runnable() { //별도 스레드를 통해 Room 데이터에 접근해야한다. 연산 시간이 오래 걸리는 작업은 메인 쓰레드가 아닌 별도의 쓰레드에서 하도록 되어있다. 그렇지 않으면 런타임에러 발생.
//                @Override
//                public void run() {
////                    gpsDao.setInsertGps(tbGps);  //DAO의 메서드들 호출
//                    runDatabase.gpsDao().setInsertGps(tbGps);
//                }
//            });
//            thread.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        db.gpsDao().setInsertGps(tbGps);
    }


    //delete 문
    //update 문
}
