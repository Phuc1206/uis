package com.example.timemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;


import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;


public class ChartActivity extends AppCompatActivity {
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        pieChart = findViewById(R.id.piechart);
        setData();
    }

    private void setData() {
        for (Task task : Task.tasksList) {
            if (task.getDeleted() == null) {
                pieChart.addPieSlice(new PieModel(task.getTitle(), task.getTime().getHour() * 60 + task.getTime().getMinute(), Color.parseColor(task.getColor())));
            }
        }
        pieChart.startAnimation();
    }
}