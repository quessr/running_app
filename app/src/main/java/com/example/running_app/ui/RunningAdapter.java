package com.example.running_app.ui;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;
import com.example.running_app.data.model.RunRepository;
import com.example.running_app.ui.viewmodels.RunViewModel;
import com.example.running_app.ui.viewmodels.TimerViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RunningAdapter extends RecyclerView.Adapter<RunningAdapter.ViewHolder> {


    private List<TB_Run> runItems = new ArrayList<>();
    private Context context;
    private Application application;
    private Activity activity;
    private RunDatabase runDatabase;

    private RunViewModel viewModel;

    int runId;

    public RunningAdapter(Application application, Activity activity) {
        super();
        this.activity = activity;
        this.application = application;
        viewModel = new RunViewModel(application);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TB_Run data = runItems.get(position);
        holder.bind(data);

        holder.list_setting.setImageResource(R.drawable.baseline_delete_24);
        holder.list_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert 창
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("운동기록을 삭제하시겠습니까?");
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(application.getApplicationContext(), "삭제 완료", Toast.LENGTH_SHORT).show();

                        //delete
                        TB_Run tbRun = new TB_Run();
                        runId = data.getRun_id();
                        tbRun.setRun_id(runId);
                        viewModel.setDeleteRun(tbRun);
                        //현재 list 화면에서 삭제시 바로 화면 반영
                        runItems.remove(holder.getAbsoluteAdapterPosition());
                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return runItems.size();
    }

    public void setRunItems(List<TB_Run> tbRuns) {
        this.runItems = tbRuns;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView t_date, t_distance, t_runTime, t_speed, t_walkCount;

        //popUpMenu
        ImageButton list_setting;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t_date = itemView.findViewById(R.id.date);
            t_distance = itemView.findViewById(R.id.distance);
            t_runTime = itemView.findViewById(R.id.run_time);
            t_speed = itemView.findViewById(R.id.speed);
            t_walkCount = itemView.findViewById(R.id.walk_count);
            list_setting = itemView.findViewById(R.id.setting);
        }

        @SuppressLint("SetTextI18n")
        public void bind(TB_Run data) {
            //timer -> string 형태로 변환
            long timeValue = data.getTimer();
            timeFormat(timeValue);

            // activeRunId = repository.getLatestRunId();
            runId = data.getRun_id();
            List<TB_GPS> allGps = viewModel.getAllGpsByRunId(runId);
            double tDistance = totalDistance(allGps);

            //평균 속력
            double avgSpeed = resultAvgSpeed(timeValue, tDistance);

            t_date.setText(data.getCreate_at());
            t_distance.setText(distanceFormat(tDistance) + " Km");
            t_runTime.setText(timeFormat(timeValue));
            t_speed.setText(speedFormat(avgSpeed) + " m/s");
            t_walkCount.setText(String.valueOf(data.getWalk_count()));
        }

        private String speedFormat(double avgSpeed) {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(avgSpeed);
        }

        private double resultAvgSpeed(long timeValue, double tDistance) {
            if (timeValue <= 0) {
                return 0.0;
            } else {
                return tDistance / timeValue * 1000;    // m/s로 변환
            }
        }

        private String distanceFormat(double tDistance) {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(tDistance);
        }

        @SuppressLint("DefaultLocale")
        private String timeFormat(long timeValue) {

            int hours = (int) (timeValue / 3600);
            int minutes = (int) ((timeValue % 3600) / 60);
            int seconds = (int) (timeValue % 60);

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    private double totalDistance(List<TB_GPS> allGps) {
        double totalDistance = 0;
        TB_GPS prevGps = null;

        for (TB_GPS currentGps : allGps) {
            if (prevGps != null) {
                // 이전 GPS와 현재 GPS 사이의 거리 계산하여 누적
                double segmentDistance = haversine(prevGps.getLat(), prevGps.getLon(), currentGps.getLat(), currentGps.getLon());
                totalDistance += segmentDistance;
            }
            prevGps = currentGps;   // 현재 GPS를 이전 GPS로 설정하여 다음 순회에 사용
        }

        return totalDistance;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        // 지구의 반경 (단위: km)
        final double R = 6371.0;

        // 라디안으로 변환
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // 위도와 경도의 차이 계산
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine 공식 적용
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산
        double distance = R * c;

        return distance;
    }
}
