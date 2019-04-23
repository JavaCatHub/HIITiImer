package com.example.android.hiittimer.timer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.ActivityTimerBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.model.Asset;
import com.example.android.hiittimer.model.CountDown;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimerActivity extends AppCompatActivity {

    private static final int RC_FITNESS_PARAM = 1;

    private ActivityTimerBinding binding;

    private TimerViewModel mViewModel;

    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("mm:ss", Locale.US);

    private CountDown count;

    private final long interval = 1000;

    private boolean current = false;

    private long countMills;

    private GoogleSignInAccount account;

    private Asset asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer);

        asset = getIntent()
                .getParcelableExtra(MainActivity.ASSET_KEY);

        TimerViewModelFactory factory = new TimerViewModelFactory(asset);
        mViewModel = ViewModelProviders.of(this, factory).get(TimerViewModel.class);

        binding.play.setSelected(false);

        binding.setViewModel(mViewModel);
        binding.timer.setText(dateFormat.format(0));
        binding.play.setOnClickListener(v ->
                {
                    if (current) {
                        current = false;
                        binding.play.setSelected(false);
                        count = new CountDown(countMills, interval);
                        count.setClockInterface(prepareInterface());
                        count.start();
                    } else {
                        current = true;
                        binding.play.setSelected(true);
                        count.cancel();
                    }
                }
        );

        mViewModel.startCountDown().observe(this, c -> {
            binding.current.setText(c.getTitle());
            binding.set.setText(String.valueOf(c.getSet()));
            binding.cycle.setText(String.valueOf(c.getCycle()));
            count = c;
            current = false;
            c.setClockInterface(prepareInterface());
            c.start();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count.cancel();
    }

    private void message() {
        Toast.makeText(this, "finish!", Toast.LENGTH_LONG).show();
    }

    private CountDown.ClockInterface prepareInterface() {
        return new CountDown.ClockInterface() {
            @Override
            public void onTickTextView(long millisUntilFinished) {
                binding.timer.setText(dateFormat.format(millisUntilFinished + 1000));
                Timber.d("%s", millisUntilFinished);
                Timber.d("%s", Math.round((double) millisUntilFinished / 1000));
                countMills = millisUntilFinished;
            }

            @Override
            public void onFinishTextView() {
                binding.timer.setText(dateFormat.format(0));
                if (!mViewModel.nextCountdown()) {
                    message();
                    if (hasFitnessPermissions()) {
                        insertSession();
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        };
    }


    private void insertSession() {
        SessionInsertRequest insertRequest = insertFitnessSession();
        Timber.i("Inserting the session in the Sessions API");
        Fitness.getSessionsClient(this, account)
                .insertSession(insertRequest)
                .addOnSuccessListener(aVoid -> Timber.i("Session insert was successful!"))
                .addOnFailureListener(e -> {
                    Timber.e(e, "There was a problem inserting the session: %s", e.getLocalizedMessage());
                });
    }


    private SessionInsertRequest insertFitnessSession() {
        Timber.i("Creating a new session");

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.MINUTE, (int) -asset.getTotalTime()/1000/60 );
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this.getPackageName())
                .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                .setName("This is test")
                .setType(DataSource.TYPE_RAW)
                .build();

        DataSet dataset = DataSet.create(dataSource);

        DataPoint point = dataset.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_ACTIVITY).setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING);
        dataset.add(point);

        Session session = new Session.Builder()
                .setName("HIIT SET")
                .setDescription("This is Test")
                .setIdentifier(getString(R.string.app_name) + " " + System.currentTimeMillis())
                .setActivity(FitnessActivities.HIGH_INTENSITY_INTERVAL_TRAINING)
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .setEndTime(endTime, TimeUnit.MILLISECONDS)
                .build();

        return new SessionInsertRequest.Builder()
                .setSession(session)
                .addDataSet(dataset)
                .build();
    }

    private boolean hasFitnessPermissions() {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            return GoogleSignIn.hasPermissions(account, Fitness.SCOPE_ACTIVITY_READ_WRITE);
        } else {
            return false;
        }
    }
}
