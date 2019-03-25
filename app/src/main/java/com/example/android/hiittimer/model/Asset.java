package com.example.android.hiittimer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Asset {

    @PrimaryKey
    private int _id;
    private String title;
    private long prepare;
    private long workOut;
    private long interval;
    private long coolDown;
    private int cycle;
    private int set;
    private long totalTime;
    private String comment;

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long calculateTotalTime() {
        this.totalTime = (this.workOut + this.interval) * this.cycle * this.set + this.coolDown * (this.set - 1) + this.prepare;
        return totalTime;
    }
}
