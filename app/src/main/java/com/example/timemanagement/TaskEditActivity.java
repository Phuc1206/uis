package com.example.timemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class TaskEditActivity extends AppCompatActivity {
    private LocalTime time,time_end;
    private EditText edtDateStart,edtTimeStart,edtTimeEnd,edtTitle,edtdes;
    private Button btnSave;
    LinearLayout layoutColor;
    private String selectedTaskColor;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        setControl();
        //time = LocalTime.now();
        selectedTaskColor = "#333333";
        SetColor();
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        time = LocalTime.of(hour,minute);
        time_end =LocalTime.of(hour,minute);
        edtDateStart.setInputType(InputType.TYPE_NULL);
        edtTimeStart.setInputType(InputType.TYPE_NULL);
        edtTimeEnd.setInputType(InputType.TYPE_NULL);
        edtDateStart.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        edtTimeStart.setText(CalendarUtils.formattedTime(time));
        edtTimeEnd.setText(CalendarUtils.formattedTime(time));
        setEvent();

    }

    private void SetColor() {
        final ImageView imageColor1 =layoutColor.findViewById(R.id.ImgColor1);
        final ImageView imageColor2 =layoutColor.findViewById(R.id.ImgColor2);
        final ImageView imageColor3 =layoutColor.findViewById(R.id.ImgColor3);
        final ImageView imageColor4 =layoutColor.findViewById(R.id.ImgColor4);
        layoutColor.findViewById(R.id.ViewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#333333";
                imageColor1.setImageResource(R.drawable.baseline_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
            }
        });
        layoutColor.findViewById(R.id.ViewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.baseline_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
            }
        });
        layoutColor.findViewById(R.id.ViewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.baseline_done);
                imageColor4.setImageResource(0);
            }
        });
        layoutColor.findViewById(R.id.ViewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.baseline_done);
            }
        });
    }

    private void setEvent() {
        edtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = LocalTime.of(hourOfDay, minute);
                        edtTimeStart.setText(CalendarUtils.formattedTime(time));
                    }
                }, time.getHour(), time.getMinute(), DateFormat.is24HourFormat(TaskEditActivity.this));
                timePickerDialog.show();

            }
        });
        edtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time_end = LocalTime.of(hourOfDay, minute);
                        edtTimeEnd.setText(CalendarUtils.formattedTime(time_end));
                    }
                }, time_end.getHour(), time_end.getMinute(), DateFormat.is24HourFormat(TaskEditActivity.this));
                timePickerDialog.show();

            }
        });
        edtDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskEditActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                                Date date = selectedDate.getTime();
                                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                CalendarUtils.selectedDate = localDate;
                                edtDateStart.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteManager sqLiteManager =SQLiteManager.instanceOfDatabase(TaskEditActivity.this);

                String taskTitle = edtTitle.getText().toString();
                String taskDes = edtdes.getText().toString();
                String taskColor = selectedTaskColor;
                Calendar calendar = Calendar.getInstance();
                calendar.set(CalendarUtils.selectedDate.getYear(), CalendarUtils.selectedDate.getMonthValue() - 1, CalendarUtils.selectedDate.getDayOfMonth(), time.getHour(), time.getMinute(), 0);
                int id = Task.tasksList.size();
                Task newTask = new Task(id,taskTitle,taskDes,taskColor, CalendarUtils.selectedDate, time,time_end);
                Task.tasksList.add(newTask);
                sqLiteManager.addNoteToDatabase(newTask);
                Intent intent = new Intent(TaskEditActivity.this,AlarmReceiver.class);
                intent.setAction("My Action");
                intent.putExtra("Desc",edtdes.getText().toString());
                intent.putExtra("Title",edtTitle.getText().toString());
                alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                pendingIntent= PendingIntent.getBroadcast(TaskEditActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                finish();
            }
        });

    }

    private void setControl() {
        edtDateStart = findViewById(R.id.edtDateStart);
        edtdes =findViewById(R.id.edtDes);
        edtTimeStart =findViewById(R.id.edtTimeStart);
        edtTimeEnd =findViewById(R.id.edtTimeEnd);
        edtTitle =findViewById(R.id.edtTitle);
        btnSave =findViewById(R.id.btnsave);
        layoutColor = findViewById(R.id.lauoutColor);
    }
}