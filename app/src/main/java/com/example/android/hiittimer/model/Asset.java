package com.example.android.hiittimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Asset {

    @PrimaryKey
    private int id;
    private String title;
    private long prepare;
    private long workOut;
    private long interval;
    private long coolDown;
    private int cycle;
    private int set;
    private long totalTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPrepare() {
        return prepare;
    }

    public void setPrepare(long prepare) {
        this.prepare = prepare;
    }

    public long getWorkOut() {
        return workOut;
    }

    public void setWorkOut(long workOut) {
        this.workOut = workOut;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(long coolDown) {
        this.coolDown = coolDown;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void calculateTotalTime() {
        this.totalTime = (this.workOut + this.interval) * this.cycle * this.set + this.coolDown * (this.set - 1) + this.prepare;
    }
}
