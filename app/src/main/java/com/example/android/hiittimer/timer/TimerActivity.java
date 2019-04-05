package com.example.android.hiittimer.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import timber.log.Timber;

import android.os.Bundle;
import android.widget.Toast;

import com.example.android.hiittimer.R;
import com.example.android.hiittimer.databinding.ActivityTimerBinding;
import com.example.android.hiittimer.main.MainActivity;
import com.example.android.hiittimer.model.CountDown;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer);

        TimerViewModelFactory factory = new TimerViewModelFactory(
                getIntent()
                        .getParcelableExtra(MainActivity.ASSET_KEY)
        );
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
                }
            }
        };
    }
}
