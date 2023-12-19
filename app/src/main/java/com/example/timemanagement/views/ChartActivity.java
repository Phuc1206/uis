package com.example.timemanagement.views;

import static com.example.timemanagement.views.Timer.totalTimeSpent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timemanagement.R;
import com.example.timemanagement.database.Task;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    Button btntoday,btnweekly,btnmonthly;
    EditText edtdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        PieChart pieChart = findViewById(R.id.chart);
        BarChart barChart = findViewById(R.id.chart2);
        btntoday = findViewById(R.id.today);
        btnweekly =findViewById(R.id.weekly);
        btnmonthly =findViewById(R.id.monthly);
        edtdate = findViewById(R.id.edtdate);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home2) {
                startActivity(new Intent(ChartActivity.this, WeekViewActivity.class));
                finish();
            } else if (itemId == R.id.thongke2) {
                return true;
            } else if (itemId == R.id.pomodoro2) {
                startActivity(new Intent(ChartActivity.this, StartPodomoroActivity.class));
                finish();
            }

            return true;
        });
        btntoday.setOnClickListener(v -> updateChart(pieChart, Task.taskForDate(LocalDate.now())));
        btnweekly.setOnClickListener(v -> updateChart(pieChart, Task.getTasksForWeek()));
        btnmonthly.setOnClickListener(v -> updateChart(pieChart, Task.getTasksForMonth()));

        updateChart(pieChart, Task.taskForDate(LocalDate.now()));
        BarEntry entry = new BarEntry(totalTimeSpent, 0); // Use "Tổng" as label if desired

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(entry);

        BarDataSet barDataSet = new BarDataSet(entries, "Thống kê");
        barDataSet.setColor(Color.parseColor("#1E88E5"));
        barDataSet.setValueTextSize(16f);
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

// Format y-axis labels to display time (replace "# minutes" with your desired format)
        barChart.getAxisLeft().setValueFormatter(new DefaultAxisValueFormatter(0) {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + " minutes";
            }
        });

        barChart.invalidate();
    }
    private void updateChart(PieChart pieChart, ArrayList<Task> tasks){
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        // Tạo một HashMap để lưu trữ số lượng công việc theo từng danh mục
        HashMap<String, Integer> categoryCounts = new HashMap<>();

        // Lấy danh sách công việc


        // Đếm số lượng công việc theo từng danh mục
        for (Task task : tasks) {
            String category = task.getCategory();
            if (categoryCounts.containsKey(category)) {
                categoryCounts.put(category, categoryCounts.get(category) + 1);
            } else {
                categoryCounts.put(category, 1);
            }
        }

        // Tạo một danh sách các PieEntry
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Thêm dữ liệu vào danh sách PieEntry
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        // Tạo PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "Task Categories");

        dataSet.setColors(colors);
        PieData pieData = new PieData(dataSet);


        pieChart.setData(pieData);
        pieChart.invalidate(); // Làm mới biểu đồ
    }
}