package com.example.running_app.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.GpsDao;
import com.example.running_app.data.database.dao.RunDao;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.List;

public class RunHistoryFragment extends Fragment {
    //전역변수 설정
    private RunDao runDao;
    private GpsDao gpsDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_run_history, container, false);

        RunDatabase database = Room.databaseBuilder(requireContext(), RunDatabase.class, "running_db")
                .fallbackToDestructiveMigration()   //스키마(= database) 버전 변경 가능
//                .addMigrations(RunDatabase.MIGRATION_1_2)
                .allowMainThreadQueries()   //Main Thread 에서 DB에 IO(입출력) 을 가능하게 함
                .build();

        runDao = database.runDao(); //인터페이스 사용 준비 완료(객체 할당)
        gpsDao = database.gpsDao();

        //데이터 삽입
        TB_Run tbRun = new TB_Run();    //객체 인스턴스 생성
        tbRun.setWalk_count(100);
        tbRun.setTimer(0.7);
        tbRun.setCreate_at("2023/10/27");
        runDao.setInsertRun(tbRun);

         TB_Run latest = runDao.getLatestActiveOne();


        //데이터 삽입
        TB_GPS tbGps = new TB_GPS();
        tbGps.setRun_id(latest.getRun_id());
        tbGps.setLat((long) 37.5564036476463);
        tbGps.setLon((long) 126.926735502823);
        tbGps.setCreate_at("2023/10/30");
        gpsDao.setInsertGps(tbGps);



        //데이터 조회
        List<TB_Run> tb_runList = runDao.getRunAll();
        for (int i = 0; i < tb_runList.size(); i++) {
            Log.d("RUN_TEST", tb_runList.get(i).getWalk_count() + "\n"
                    + tb_runList.get(i).getTimer() + "\n"
                    + tb_runList.get(i).getCreate_at());
        }

//        //데이터 조회
//        List<TB_GPS> tb_gpsList = gpsDao.getGpsAll();
//        for (int i = 0; i < tb_gpsList.size(); i++){
//            Log.d("GPS_TEST", tb_gpsList.get(i).toString());
//        }


//        //데이터 수정
//        TB_Run tbRun2 = new TB_Run();    //객체 인스턴스 생성
//        tbRun2.setRun_id(1);
//        tbRun2.setWalk_count(150);
//        tbRun2.setTimer(20.0);
//        tbRun2.setCreate_at("2023/10/28");
//        tbRun2.setTotal_distance(42.195);
//        runDao.setUpdateRun(tbRun2);


//        //데이터 삭제
//        TB_Run tbRun3= new TB_Run();    //객체 인스턴스 생성
//        tbRun3.setRun_id(2);
//        runDao.setDeleteRun(tbRun3);





        return view;
    }

}
