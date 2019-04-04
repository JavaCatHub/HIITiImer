package com.example.android.hiittimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Asset implements Parcelable {

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

    public void setDefaultMyself(){
        setTitle("Default");
        setPrepare(10000);
        setWorkOut(20000);
        setInterval(10000);
        setCoolDown(60000);
        setCycle(8);
        setSet(2);
        setTotalTime(calculateTotalTime());
        setComment("This is the test");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.title);
        dest.writeLong(this.prepare);
        dest.writeLong(this.workOut);
        dest.writeLong(this.interval);
        dest.writeLong(this.coolDown);
        dest.writeInt(this.cycle);
        dest.writeInt(this.set);
        dest.writeLong(this.totalTime);
        dest.writeString(this.comment);
    }

    public Asset() {
    }

    protected Asset(Parcel in) {
        this._id = in.readInt();
        this.title = in.readString();
        this.prepare = in.readLong();
        this.workOut = in.readLong();
        this.interval = in.readLong();
        this.coolDown = in.readLong();
        this.cycle = in.readInt();
        this.set = in.readInt();
        this.totalTime = in.readLong();
        this.comment = in.readString();
    }

    public static final Parcelable.Creator<Asset> CREATOR = new Parcelable.Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel source) {
            return new Asset(source);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };
}