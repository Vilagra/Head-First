package com.example.timer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    int seconds;
    boolean isStarted= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        runTimer();
    }
    public void start(View view){
        isStarted=true;
    }
    public void stop(View view){
        isStarted=false;
    }
    public void reset(View view){
        seconds=0;
        isStarted=false;
    }
    public void other(View view){
        startActivity(new Intent(this,OtherActivity.class));
    }




    public void runTimer(){
        final TextView timer = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                timer.setText(time);
                if(isStarted){
                    seconds++;
                }
                handler.postDelayed(this,1000);
            }
        });
    }

}
