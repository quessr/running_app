package com.example.running_app.ui;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.running_app.R;
import com.example.running_app.data.database.dao.RunDatabase;
import com.example.running_app.data.database.dao.TB_GPS;
import com.example.running_app.data.database.dao.TB_Run;

import java.util.ArrayList;
import java.util.List;

public class RunningAdapter extends RecyclerView.Adapter<RunningAdapter.ViewHolder> {
    private List<TB_Run> runItems = new ArrayList<>();
    private List<TB_GPS> gpsItems;
    private Context context;
    private RunDatabase runDatabase;

    public RunningAdapter(Context context){
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return runItems.size();
    }

    public void setRunItems(List<TB_Run> tbRuns) {
        this.runItems = tbRuns;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView t_date, t_distance, t_runTime, t_speed, t_walkCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t_date = itemView.findViewById(R.id.date);
            t_distance = itemView.findViewById(R.id.distance);
            t_runTime = itemView.findViewById(R.id.run_time);
            t_speed = itemView.findViewById(R.id.speed);
            t_walkCount = itemView.findViewById(R.id.walk_count);
        }

        @SuppressLint("SetTextI18n")
        public void bind(TB_Run data) {
            t_date.setText(data.getCreate_at());
            t_distance.setText("15km");
            t_runTime.setText(data.getTimer());
//            t_runTime.setText(format(String.valueOf(data.getTimer())));
//            t_runTime.setText((int) data.getTimer());
            t_speed.setText("5km");
            t_walkCount.setText(String.valueOf(data.getWalk_count()));
        }
    }
}
