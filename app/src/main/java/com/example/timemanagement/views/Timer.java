package com.example.timemanagement.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timemanagement.R;

import java.util.Locale;

public class Timer extends AppCompatActivity {

    private TextView txt_time;
    private TextView txt_breaks;
    private ImageView img_play;
    private ImageView img_replay,img_tua;
    private ProgressBar progressbar;
    private Integer timerLength;
    private int breaksCount = 0; // Tổng số breaks (short + long)
    private int shortBreaksCount = 0; // Số short breaks
    private Integer totalBreaks,shortBreakLength,longBreakLength;

    private Button btnChangeTime;
    private int currentBreaks = 0;
    private enum Task {
        WORK,
        SHORT_BREAK,
        LONG_BREAK
    }
    private Task currentTask = Task.WORK;
    private enum TimerStatus {
        STARTED, STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STARTED;
    private long timeLeftInMillis = 1 * 60000;
    private long timeShortBreakInMillis =1*60000;
    private long timeLongBreakInMillis =1*60000;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private boolean sessionCompleted = false;
    public static long totalTimeSpent = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Khởi tạo các thành phần
        init();
        // Nhận dữ liệu từ Intent
        getData();

        // Xử lý sự kiện khi click nút play/pause
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerStatus == TimerStatus.STARTED) {
                    pauseTimer();
                } else {
//                    startTimer();
                    switch (currentTask) {
                        case WORK:

                            startTimer();
                            break;
                        case SHORT_BREAK:

                            startShortBreak();
                            break;
                        case LONG_BREAK:

                            startLongBreak();
                            break;
                    }
                }
            }
        });

        // Xử lý sự kiện khi click nút replay
        img_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentTask) {
                    case WORK:
                        resetTimer(timeLeftInMillis);
                        startTimer();
                        break;
                    case SHORT_BREAK:
                        // Thiết lập lại thời gian nghỉ ngắn
                        resetTimer(timeShortBreakInMillis);
                        startShortBreak();
                        break;
                    case LONG_BREAK:
                        // Thiết lập lại thời gian nghỉ dài

                        resetTimer(timeLongBreakInMillis);
                        startLongBreak();
                        break;
                }
            }
        });
        img_tua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Timer.this);
                builder.setMessage("Bạn có chắc chắn muốn bỏ qua thời gian không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        pauseTimer();
                        switch (currentTask) {
                            case WORK:
                                showCompletionDialogStart();
                                break;
                            case SHORT_BREAK:
                                showCompletionDialogLongBreak();
                                break;
                            case LONG_BREAK:
                                showCompletionDialogNewSession();
                                break;
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        // Xử lý sự kiện khi click nút thay đổi thời gian
        btnChangeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

    }

    // Khởi tạo các thành phần
    private void init() {
        txt_time = findViewById(R.id.txt_time_play);
        txt_breaks = findViewById(R.id.txt_total_breaks);
        progressbar = findViewById(R.id.progressBar);
        img_play = findViewById(R.id.img_play);
        img_replay = findViewById(R.id.img_replay);
        btnChangeTime = findViewById(R.id.btn_change_time);
        img_tua = findViewById(R.id.img_tua);
    }
    // Nhận dữ liệu từ Intent
    private void getData() {
        timerLength = getIntent().getIntExtra("TIME", 1);
        totalBreaks = getIntent().getIntExtra("BREAKS", 1);
        shortBreakLength =getIntent().getIntExtra("SHORT_BREAK",3);
        longBreakLength =getIntent().getIntExtra("LONG_BREAK",15);


        if (totalBreaks == 0) {
            txt_breaks.setVisibility(View.GONE);
        } else {
            txt_breaks.setText(breaksCount + " of " + totalBreaks + " session");
        }
        timeShortBreakInMillis =(shortBreakLength*60*1000L);
        timeLeftInMillis = (timerLength * 60 * 1000L);
        timeLongBreakInMillis=(longBreakLength*60*1000L);
        startTimer();
    }

    // Cài đặt giá trị cho thanh ProgressBar
    private void setProgressBarValues() {
        switch (currentTask) {
            case WORK:
                timeLeftInMillis = (timerLength * 60 * 1000L);
                progressbar.setMax((int) (timeLeftInMillis / 1000));
                progressbar.setProgress((int) (timeLeftInMillis / 1000));
                break;
            case SHORT_BREAK:
                timeLeftInMillis = (shortBreakLength * 60 * 1000L);
                progressbar.setMax((int) (timeLeftInMillis / 1000));
                progressbar.setProgress((int) (timeLeftInMillis / 1000));
                break;
            case LONG_BREAK:
                timeLeftInMillis = (longBreakLength * 60 * 1000L);
                progressbar.setMax((int) (timeLeftInMillis / 1000));
                progressbar.setProgress((int) (timeLeftInMillis / 1000));
                break;
        }
    }
    // Hiển thị hộp thoại chọn thời gian
    private void showTimePickerDialog() {
        CustomSeekbar timePickerDialog = new CustomSeekbar();
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    // Bắt đầu đồng hồ đếm ngược
    private void startTimer() {
        currentTask = Task.WORK;
        setProgressBarValues();
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMillis = millisUntilFinished;
                img_play.setImageResource(R.drawable.ic_pause);
                txt_breaks.setText( breaksCount + " of " + totalBreaks + " session");
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                txt_time.setText(timeLeftFormatted);
                progressbar.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                totalTimeSpent += timerLength * 60 * 1000L;
                timerStatus = TimerStatus.STOPPED;
                showCompletionDialogStart();
            }
        }.start();
        timerStatus = TimerStatus.STARTED;
        isTimerRunning = true;
    }

    // Bắt đầu short break
    private void startShortBreak() {
        currentTask = Task.SHORT_BREAK;
        setProgressBarValues();
        countDownTimer = new CountDownTimer(timeShortBreakInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeShortBreakInMillis = millisUntilFinished;
                img_play.setImageResource(R.drawable.ic_pause);
                txt_breaks.setText( breaksCount + " of " + totalBreaks + " session");
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                txt_time.setText(timeLeftFormatted);
                progressbar.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                totalTimeSpent += shortBreakLength * 60 * 1000L;
                timerStatus = TimerStatus.STOPPED;
                showCompletionDialogLongBreak();
            }

        }.start();
        timerStatus = TimerStatus.STARTED;
        isTimerRunning = true;
    }
    // Bắt đầu long break
    private void startLongBreak() {
        currentTask = Task.LONG_BREAK;
        setProgressBarValues();
        countDownTimer = new CountDownTimer(timeLongBreakInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLongBreakInMillis = millisUntilFinished;
                img_play.setImageResource(R.drawable.ic_pause);
                txt_breaks.setText( breaksCount + " of " + totalBreaks + " session");
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                txt_time.setText(timeLeftFormatted);
                progressbar.setProgress((int) (millisUntilFinished / 1000));
//                progressbar.setMax((int) (millisUntilFinished / 1000));
//                progressbar.setProgress((int) (millisUntilFinished / 1000));

            }


            @Override
            public void onFinish() {
                totalTimeSpent += longBreakLength * 60 * 1000L;
                timerStatus = TimerStatus.STOPPED;
                showCompletionDialogNewSession();
            }
        }.start();
        timerStatus = TimerStatus.STARTED;
        isTimerRunning = true;
    }
    private void showCompletionDialogStart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn chuyển sang thời gian nghỉ không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Người dùng chọn "Có"
                        // Thực hiện các bước cần thiết (chẳng hạn, chuyển sang thời gian nghỉ)
                        countDownTimer.cancel();
                        startShortBreak();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Người dùng chọn "Không"
                        // Thực hiện các bước cần thiết (chẳng hạn, hoàn thành 1 session và bắt đầu thời gian mới)
                        showCompletionDialogNewSession();
                    }


                })
                .setCancelable(false) // Ngăn chặn việc đóng hộp thoại bằng cách nhấn ra ngoài
                .show();
    }
    private void showCompletionDialogLongBreak() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn chuyển sang thời gian nghỉ dài không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Thực hiện các bước cần thiết (chẳng hạn, chuyển sang thời gian nghỉ dài)
                        countDownTimer.cancel();
                        startLongBreak();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Thực hiện các bước cần thiết (chẳng hạn, hoàn thành 1 session và bắt đầu thời gian mới)
                        showCompletionDialogNewSession();
                    }
                })
                .setCancelable(false) // Ngăn chặn việc đóng hộp thoại bằng cách nhấn ra ngoài
                .show();
    }


    // Tạm dừng đồng hồ đếm ngược
    private void pauseTimer() {
        countDownTimer.cancel();
        timerStatus = TimerStatus.STOPPED;
        img_play.setImageResource(R.drawable.ic_play);
        isTimerRunning = false;
    }

    // Đặt lại đồng hồ đếm ngược
    private void resetTimer(long time) {
        // Hủy CountDownTimer hiện tại
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Thiết lập lại thời gian còn lại
        timeLeftInMillis = time;

        // Cập nhật thanh ProgressBar
        progressbar.setMax((int) (timeLeftInMillis / 1000));
        progressbar.setProgress((int) (timeLeftInMillis / 1000));

        // Thiết lập lại trạng thái
        img_play.setImageResource(R.drawable.ic_play);
//        countDownTimer = null;
    }

    // Cập nhật văn bản hiển thị thời gian còn lại
//    private void updateCountDownText() {
//        switch (currentTask) {
//            case WORK:
//                timeLeftInMillis = (timerLength * 60 * 1000L);
//                int minutes = (int) (timeLeftInMillis / 1000) / 60;
//                int seconds = (int) (timeLeftInMillis / 1000) % 60;
//                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                txt_time.setText(timeLeftFormatted);
//                break;
//            case SHORT_BREAK:
//                timeLeftInMillis = (shortBreakLength * 60 * 1000L);
//                int minutes = (int) (timeLeftInMillis / 1000) / 60;
//                int seconds = (int) (timeLeftInMillis / 1000) % 60;
//                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                txt_time.setText(timeLeftFormatted);
//                break;
//            case LONG_BREAK:
//                timeLeftInMillis = (longBreakLength * 60 * 1000L);
//                int minutes = (int) (timeLeftInMillis / 1000) / 60;
//                int seconds = (int) (timeLeftInMillis / 1000) % 60;
//                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                txt_time.setText(timeLeftFormatted);
//                break;
//        }
//    }

    // Hiển thị hộp thoại xác nhận khi kết thúc thời gian

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//        myEdit.putLong("timeLeft", timeLeftInMillis);
//        myEdit.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        timeLeftInMillis = sharedPreferences.getLong("timeLeft", 0);
//        if (timeLeftInMillis < 1000) {
//            timeLeftInMillis = timerLength * 60 * 1000L;
//        }
//        if (!isTimerRunning) {
//            startTimer();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//        myEdit.clear();
//        myEdit.apply();
    }

    private void showCompletionDialogNewSession() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn tiếp tục làm việc không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Người dùng chọn "Có"
                        // Thực hiện các bước cần thiết (chẳng hạn, chuyển sang thời gian nghỉ)
                        breaksCount++;
                        if (breaksCount < totalBreaks) {
                            startTimer();
                        } else {
                            sessionCompleted =true;
                        }
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Người dùng chọn "Không"
                        // Thực hiện các bước cần thiết (chẳng hạn, hoàn thành 1 session và bắt đầu thời gian mới)
                        pauseTimer();
                        breaksCount++;
                        sessionCompleted =true;
                        //lưu thời gian lại
                    }
                })
                .setCancelable(false) // Ngăn chặn việc đóng hộp thoại bằng cách nhấn ra ngoài
                .show();
    }
}
