package com.example.android.hiittimer.timer;

import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.model.CountDown;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class TimerViewModel extends ViewModel {

    private MutableLiveData<CountDown> countDown = new MutableLiveData<>();

    private Iterator<CountDown> iterator;

    private Asset asset;

    public TimerViewModel(List<CountDown> timerList, Asset asset) {
        this.iterator = timerList.iterator();
        this.asset = asset;
    }

    private MutableLiveData<CountDown> getCountDown() {
        return countDown;
    }

    private void setCountDown(CountDown countDown) {
        this.countDown.setValue(countDown);
    }

    public MutableLiveData<CountDown> startCountDown() {
        nextCountdown();
        return getCountDown();
    }

    boolean nextCountdown() {
        if (iterator.hasNext()) {
            setCountDown(iterator.next());
            return true;
        }
        return false;
    }

    public SessionInsertRequest insertFitnessSession(String packageName, String appName) {
        Timber.i("Creating a new session");

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.MILLISECOND,(int) -asset.getTotalTime());
        long startTime = cal.getTimeInMillis();
        Timber.i("start time is %s. end time is %s. total time is %s mSec.",startTime,endTime, -asset.getTotalTime() /1000);

        DataSource dataSource = buildDataSource(packageName);

        DataSet dataset = DataSet.create(dataSource);

        setDataPoint(dataset,startTime,endTime);

        Session session = getSession(appName, startTime, endTime);

        return new SessionInsertRequest.Builder()
                .setSession(session)
                .addDataSet(dataset)
                .build();
    }

    private DataSource buildDataSource(String packageName) {
        return new DataSource.Builder()
                .setAppPackageName(packageName)
                .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                .setName("This is test")
                .setType(DataSource.TYPE_RAW)
                .build();
    }

    private void setDataPoint(DataSet dataSet, long startTime, long endTime) {
        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_ACTIVITY).setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING);
        dataSet.add(point);
    }

    private Session getSession(String appName, long startTime, long endTime) {
        return new Session.Builder()
                .setName("HIIT SET")
                .setDescription("This is Test")
                .setIdentifier(appName + " " + System.currentTimeMillis())
                .setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING)
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .setEndTime(endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    public Asset getAsset() {
        return this.asset;
    }
}
