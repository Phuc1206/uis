package com.example.timemanagement.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timemanagement.notification.AlarmReceiver;
import com.example.timemanagement.adapter.CalendarUtils;
import com.example.timemanagement.notification.NotificationReceiver;
import com.example.timemanagement.R;
import com.example.timemanagement.database.SQLiteManager;
import com.example.timemanagement.database.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class TaskEditActivity extends AppCompatActivity {
    private LocalTime time,time_end;
    private EditText edtDateStart,edtTimeStart,edtTimeEnd,edtTitle,edtdes;
    private Button btnSave,btnwork,btnread,btnlearn;
    LinearLayout layoutColor;
    private String selectedTaskColor;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private String Category= null;
    Switch alarmSwitch;
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
//        edtTimeEnd.setInputType(InputType.TYPE_NULL);
        edtDateStart.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        edtTimeStart.setText(CalendarUtils.formattedTime(time));
//        edtTimeEnd.setText(CalendarUtils.formattedTime(time));
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
//        edtTimeEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskEditActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        time_end = LocalTime.of(hourOfDay, minute);
//                        edtTimeEnd.setText(CalendarUtils.formattedTime(time_end));
//                    }
//                }, time_end.getHour(), time_end.getMinute(), DateFormat.is24HourFormat(TaskEditActivity.this));
//                timePickerDialog.show();
//
//            }
//        });
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
        btnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category = btnread.getText().toString();
                btnread.setBackgroundColor(Color.GREEN);
                btnwork.setBackgroundColor(Color.GRAY);
                btnlearn.setBackgroundColor(Color.GRAY);
            }
        });
        btnlearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category = btnlearn.getText().toString();
                btnlearn.setBackgroundColor(Color.GREEN);
                btnwork.setBackgroundColor(Color.GRAY);
                btnread.setBackgroundColor(Color.GRAY);
            }
        });
        btnwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category = btnwork.getText().toString();
                btnwork.setBackgroundColor(Color.GREEN);
                btnread.setBackgroundColor(Color.GRAY);
                btnlearn.setBackgroundColor(Color.GRAY);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(TaskEditActivity.this);
                if (edtTitle.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TaskEditActivity.this, "Please enter a valid title", Toast.LENGTH_SHORT).show();
                    edtTitle.setError("Title is required");
                    edtTitle.requestFocus();
                } else if (edtdes.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(TaskEditActivity.this, "Please enter a valid description", Toast.LENGTH_SHORT).show();
                    edtdes.setError("Description is required");
                    edtTitle.requestFocus();
                } else if (Category==null) {
                    Toast.makeText(TaskEditActivity.this,"Please chosse category",Toast.LENGTH_SHORT).show();
                } else {
                    String taskTitle = edtTitle.getText().toString();
                    String taskDes = edtdes.getText().toString();
                    String taskColor = selectedTaskColor;
                    String taskCategory = Category;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(CalendarUtils.selectedDate.getYear(), CalendarUtils.selectedDate.getMonthValue() - 1, CalendarUtils.selectedDate.getDayOfMonth(), time.getHour(), time.getMinute(), 0);
                    int id = Task.tasksList.size();
                    Task newTask = new Task(id, taskTitle, taskDes, taskColor, CalendarUtils.selectedDate, time, time_end,taskCategory);
                    Task.tasksList.add(newTask);
                    sqLiteManager.addNoteToDatabase(newTask);
                    if (alarmSwitch.isChecked()) {
                        Intent intent = new Intent(TaskEditActivity.this, AlarmReceiver.class);
                        intent.setAction("My Action");
                        intent.putExtra("Desc", edtdes.getText().toString());
                        intent.putExtra("Title", edtTitle.getText().toString());
                        intent.putExtra("Color", taskColor);
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(TaskEditActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        finish();
                    } else {
                        Intent intent = new Intent(TaskEditActivity.this, NotificationReceiver.class);
                        intent.setAction("My Action 2");
                        intent.putExtra("Desc", edtdes.getText().toString());
                        intent.putExtra("Title", edtTitle.getText().toString());
                        intent.putExtra("Color", taskColor);
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(TaskEditActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        finish();

                    }
                }
            }
        });

    }

    private void setControl() {
        edtDateStart = findViewById(R.id.edtDateStart);
        edtdes =findViewById(R.id.edtDes);
        edtTimeStart =findViewById(R.id.edtTimeStart);
//        edtTimeEnd =findViewById(R.id.edtTimeEnd);
        edtTitle =findViewById(R.id.edtTitle);
        btnSave =findViewById(R.id.btnsave);
        layoutColor = findViewById(R.id.lauoutColor);
        alarmSwitch = findViewById(R.id.alarmSwitch);
        btnlearn = findViewById(R.id.learn);
        btnread = findViewById(R.id.read);
        btnwork = findViewById(R.id.work);
    }
}