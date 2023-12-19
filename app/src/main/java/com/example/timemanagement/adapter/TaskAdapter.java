package com.example.timemanagement.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.timemanagement.R;
import com.example.timemanagement.database.Task;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(@NonNull Context context, List<Task> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Task task = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_cell, parent, false);
        CardView cardView = convertView.findViewById(R.id.layoutTask);
        TextView tvTitle = convertView.findViewById(R.id.tvtitle);
        TextView tvdes = convertView.findViewById(R.id.tvdes);
        TextView tvtime = convertView.findViewById(R.id.tvtime);
        tvTitle.setText(task.getTitle().toString());
        tvdes.setText(task.getDes().toString());
        tvtime.setText(task.getTime().toString());
        if (isOvertime(task)) {
            // Change the color of your view items
            cardView.setBackgroundColor(Color.RED);
        } else {
            cardView.setBackgroundColor(Color.parseColor(task.getColor()));
        }

        return convertView;
    }

    private boolean isOvertime(Task task) {
        return task.getTime().isAfter(task.getTime_end());
    }
}
