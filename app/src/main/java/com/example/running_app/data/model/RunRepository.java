package com.example.running_app.data.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.running_app.data.database.dao.GpsDao;
import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.List;

public class RunRepository {
    RunDao runDao;
    GpsDao gpsDao;
    RunDatabase runDatabase;
    LiveData<List<TB_Run>> getRunAll;

    List<TB_Run> getLatestActiveOne;

    public RunRepository(Application application){
        runDatabase = RunDatabase.getInstance(application);
        getRunAll = runDatabase.runDao().getRunAll();
//        getLatestActiveOne = runDatabase.runDao().getLatestActiveOne();
    }

   public LiveData<List<TB_Run>> getRunAll(){
        return getRunAll;
    }

    public List<TB_Run> getLatestActiveOne() {
        return runDatabase.runDao().getLatestActiveOne();
    }

    //insert 문
    public void setInsertRun(TB_Run tbRun) {
        runDatabase.runDao().setInsertRun(tbRun);
//        try {
//            Thread thread = new Thread(new Runnable() { //별도 스레드를 통해 Room 데이터에 접근해야한다. 연산 시간이 오래 걸리는 작업은 메인 쓰레드가 아닌 별도의 쓰레드에서 하도록 되어있다. 그렇지 않으면 런타임에러 발생.
//                @Override
//                public void run() {
////                    runDao.setInsertRun(tbRun);  //DAO의 메서드들 호출
//                    runDatabase.runDao().setInsertRun(tbRun);
//                }
//            });
//            thread.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setInsertGps(TB_GPS tbGps) {
        try {
            Thread thread = new Thread(new Runnable() { //별도 스레드를 통해 Room 데이터에 접근해야한다. 연산 시간이 오래 걸리는 작업은 메인 쓰레드가 아닌 별도의 쓰레드에서 하도록 되어있다. 그렇지 않으면 런타임에러 발생.
                @Override
                public void run() {
//                    gpsDao.setInsertGps(tbGps);  //DAO의 메서드들 호출
                    runDatabase.gpsDao().setInsertGps(tbGps);
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //delete 문
    //update 문
}
