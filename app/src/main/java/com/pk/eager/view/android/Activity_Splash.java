package com.pk.eager.view.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pk.eager.Dashboard;
import com.pk.eager.R;
import com.pk.eager.core.controller.Run;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utility.initialize(getResources());
        Run.initialize();

        final Activity_Splash activitySplash = this;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(activitySplash, Dashboard.class);
                        activitySplash.startActivity(intent);
                        activitySplash.finish();
                    }
                });
            }
        };

        timer.schedule(task, 1000);

    }
}
