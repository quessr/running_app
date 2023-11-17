package com.example.running_app.data.model;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.running_app.data.database.dao.TB_Run;

public interface OnRunHistoryItemClickListener {
    void onItemClickListener(TB_Run item, String distanceFormat, String speedFormat, String timeFormat);
}
