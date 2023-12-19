package com.example.timemanagement.database;

import java.io.Serializable;

public class Podomoro implements Serializable {
    private int time,shortBreak,longBreak;
    private int breaks;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getBreaks() {
        return breaks;
    }

    public void setBreaks(int breaks) {
        this.breaks = breaks;
    }

    public int getShortBreak() {
        return shortBreak;
    }

    public void setShortBreak(int shortBreak) {
        this.shortBreak = shortBreak;
    }

    public int getLongBreak() {
        return longBreak;
    }

    public void setLongBreak(int longBreak) {
        this.longBreak = longBreak;
    }

    public Podomoro() {
    }

    public Podomoro(int time, int breaks,int shortBreak,int longBreak) {
        this.time = time;
        this.breaks = breaks;
        this.shortBreak =shortBreak;
        this.longBreak =longBreak;
    }
}
