package com.example.android.hiittimer.timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.ActivityTimerBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.model.CountDown;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private ActivityTimerBinding binding;

    private TimerViewModel mViewModel;

    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("mm:ss", Locale.US);

    private CountDown count;

    private final long interval = 1000;

    private boolean current = false;

    private long countMills;

    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer);

        initializeViewModel();

        binding.play.setSelected(false);
        binding.setViewModel(mViewModel);
        binding.timer.setText(dateFormat.format(0));
        binding.play.setOnClickListener(v ->
                {
                    if (current) {
                        restartCountDown();
                    } else {
                        interruptCountDown();
                    }
                }
        );

        mViewModel.startCountDown().observe(this, c -> {
            setTexts(c);
            count = c;
            current = false;
            c.setClockInterface(prepareInterface());
            c.start();
        });
    }

    @Override
    public void onBackPressed() {
        if (hasFitnessPermissions()) {
            initializeAlertDialog(this);
        } else {
            count.cancel();
            finish();
        }
    }

    private void restartCountDown() {
        current = false;
        binding.play.setSelected(false);
        count = new CountDown(countMills, interval);
        count.setClockInterface(prepareInterface());
        count.start();
    }

    private void interruptCountDown() {
        current = true;
        binding.play.setSelected(true);
        count.cancel();
    }

    private void initializeViewModel() {
        TimerViewModelFactory factory = new TimerViewModelFactory(getIntent()
                .getParcelableExtra(MainActivity.ASSET_KEY));
        mViewModel = ViewModelProviders.of(this, factory).get(TimerViewModel.class);

    }

    private void initializeAlertDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Your record is not saved. \nAre you sure you want to exit?")
                .setPositiveButton("Continue", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Finish", (dialog, which) -> {
                    dialog.dismiss();
                    count.cancel();
                    finish();
                }).show();
    }

    private void setTexts(CountDown c) {
        binding.current.setText(c.getTitle());
        binding.set.setText(String.valueOf(c.getSet()));
        binding.cycle.setText(String.valueOf(c.getCycle()));
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
        Timber.i("Inserting the session in the Sessions API");
        Fitness.getSessionsClient(this, account)
                .insertSession(
                        mViewModel.insertFitnessSession(
                                this.getPackageName(),
                                getString(R.string.app_name)))
                .addOnSuccessListener(aVoid -> Timber.i("Session insert was successful!"))
                .addOnFailureListener(e -> {
                    Timber.e(e, "There was a problem inserting the session: %s", e.getLocalizedMessage());
                });
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
