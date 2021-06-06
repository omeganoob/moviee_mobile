package org.meicode.appfilm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.meicode.appfilm.R;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ProgressBar progressBar;
    TextView txtView;
    LottieAnimationView lottie;
    private int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress);
        txtView = findViewById(R.id.txtView);
        lottie = findViewById(R.id.lottie);
        progressBar.setProgress(0);
        txtView.setText("");
        final long period = 40;
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if(i<100){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtView.setText(String.valueOf(i)+"%");
                        }
                    });
                    progressBar.setProgress(i);
                    i++;
                }
                else{
                    time.cancel();
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                    progressBar.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
                    txtView.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
                    lottie.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
                    finish();
                }
            }
        },0,period);
    }
    }
