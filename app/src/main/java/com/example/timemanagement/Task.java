package com.example.timemanagement;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Task {
    public static ArrayList<Task> tasksList = new ArrayList<>();
    public static String TASK_EDIT_EXTRA = "taskEdit";

    public Task() {
    }

    public static ArrayList<Task> taskForDate(LocalDate date)
    {
        ArrayList<Task> tasks = new ArrayList<>();

        for(Task task : tasksList)
        {
            if(task.getDate().equals(date) && task.getDeleted()==null)
                tasks.add(task);
        }

        return tasks;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private int id;

    private String title,des,color;
    private Date deleted;
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    private LocalDate date;
    private LocalTime time,time_end;

    public LocalTime getTime_end() {
        return time_end;
    }

    public void setTime_end(LocalTime time_end) {
        this.time_end = time_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
    public static Task getTaskForID(int passedTaskID)
    {
        for(Task task: tasksList){
            if (task.getId()== passedTaskID)
                return task;
        }
        return null;
    }


    public Task(int id, String title, String des,String color, Date deleted, LocalDate date, LocalTime time,LocalTime time_end) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.color = color;
        this.deleted = deleted;
        this.date = date;
        this.time = time;
        this.time_end = time_end;
    }

    public Task(int id, String title, String des,String color, LocalDate date, LocalTime time,LocalTime time_end)
    {
        this.id = id;
        this.title = title;
        this.des =des;
        this.color = color;
        this.date = date;
        this.time = time;
        this.time_end = time_end;
        deleted = null;

    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

}

