package com.example.android.hiittimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import com.example.android.hiittimer.databinding.ActivityTimerBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private ActivityTimerBinding binding;

    private TimerViewModel mViewModel;

    private SimpleDateFormat dateFormat =
            new SimpleDateFormat("mm:ss", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timer);

        TimerViewModelFactory factory = new TimerViewModelFactory(
                getIntent()
                        .getParcelableExtra(MainActivity.ASSET_KEY)
        );
        mViewModel = ViewModelProviders.of(this, factory).get(TimerViewModel.class);

        binding.setViewModel(mViewModel);
        binding.timer.setText(dateFormat.format(0));

        mViewModel.startCountDown().observe(this, c -> {
            c.setClockInterface(new CountDown.ClockInterface() {
                @Override
                public void onTickTextView(long millisUntilFinished) {
                    binding.timer.setText(dateFormat.format(millisUntilFinished));
                }

                @Override
                public void onFinishTextView() {
                    binding.timer.setText(dateFormat.format(0));
                    if (!mViewModel.nextCountdown()){
                        message();
                    }
                }
            });
            c.start();
        });
    }

    private void message(){
        Toast.makeText(this,"finish!",Toast.LENGTH_LONG).show();
    }
}
