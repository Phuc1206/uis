package com.example.timemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class DetailTaskActivity extends AppCompatActivity {
    TextView tvTitle,tvDate,tvTime;
    Button btnchange;
    Button btnSave,btndel;
    Task selectedTask;
    EditText edtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        setControl();
        checkForEditTask();
        setEvent();
    }

    private void setEvent() {
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTask.setDeleted(new Date());
                SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(DetailTaskActivity.this);
                sqLiteManager.updateNoteInDB(selectedTask);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteManager sqLiteManager =SQLiteManager.instanceOfDatabase(DetailTaskActivity.this);
                String title = String.valueOf(edtTitle.getText());
                String time = String.valueOf(tvTime.getText());
                String date = String.valueOf(tvDate.getText());
                selectedTask.setTitle(title);
                selectedTask.setTime(LocalTime.parse(time));
                selectedTask.setDate(LocalDate.parse(date));
                sqLiteManager.updateNoteInDB(selectedTask);
                finish();
            }
        });
      btnchange.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              tvTitle.setVisibility(View.GONE);
              edtTitle.setVisibility(View.VISIBLE);
              edtTitle.setText(tvTitle.getText());
          }
      });

    }

    private void checkForEditTask() {
        Intent previousIntent = getIntent();
        int passedTaskID = previousIntent.getIntExtra(Task.TASK_EDIT_EXTRA,-1);
        selectedTask = Task.getTaskForID(passedTaskID);
        if (selectedTask != null){
            tvTitle.setText(selectedTask.getTitle());
            tvTime.setText(selectedTask.getTime().toString());
            tvDate.setText(selectedTask.getDate().toString());
        }
    }

    private void setControl() {
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDate = findViewById(R.id.tvDetailDate);
        tvTime = findViewById(R.id.tvDetailTime);
        btnchange = findViewById(R.id.btnchange);
        btndel =findViewById(R.id.btnDeleteTask);
        btnSave = findViewById(R.id.btnComplete);
        edtTitle = findViewById(R.id.edtDetailTitle);
    }
}