package com.example.tapeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public long duration=0,preDuration=0,timelineSeconds=0;
    public EditText editTextHH,editTextMM,editTextSS;
    public TextView textViewStatus, textViewSeconds;
    public ImageButton btnRewind,btnStop,btnPlay,btnFastForward;
    boolean isPlay = true;
    private int counter = 0;
    private CountDownTimer countDownTimer = new CountDownTimer(1,1) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextHH = findViewById(R.id.hh);
        editTextMM = findViewById(R.id.mm);
        editTextSS = findViewById(R.id.ss);

        textViewStatus = findViewById(R.id.status);
        textViewSeconds = findViewById(R.id.timeline);
        btnRewind = findViewById(R.id.rewinding);
        btnStop = findViewById(R.id.stop);
        btnPlay = findViewById(R.id.play);
        btnFastForward = findViewById(R.id.fastForward);

        btnRewind.setBackground(getDrawable(R.drawable.ic_baseline_fast_rewind_24));
        btnStop.setBackground(getDrawable(R.drawable.ic_baseline_stop_24));
        btnPlay.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
        btnFastForward.setBackground(getDrawable(R.drawable.ic_baseline_fast_forward_24));
        textViewStatus.setText("Stopped");

        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = true;
                btnPlay.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
                countDownTimer.cancel();
                long d = timelineSeconds;
                countDownTimer = new CountDownTimer((d*1000)/10, 100) {

                    public void onTick(long millisUntilFinished) {
                        timelineSeconds -= 1;
                        textViewStatus.setText("Rewinding");
                        textViewSeconds.setText(toSecondsString(timelineSeconds));
                    }
                    public void onFinish() {
                        resetTimeline();
                    }
                }.start();
            }
        });
        
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = true;
                countDownTimer.cancel();
                resetTimeline();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                int h=0,m=0,s=0;
                try {
                    h = Integer.parseInt(editTextHH.getText().toString());
                    m = Integer.parseInt(editTextMM.getText().toString());
                    s = Integer.parseInt(editTextSS.getText().toString());
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                }
                duration = (long) h*60*60 + m*60 + s;
                if(duration==0) {
                    Toast.makeText(getApplicationContext(),"Please input duration",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(duration != preDuration)  {                  //if the input duration changed
                    timelineSeconds = 0;
                    preDuration = duration;
                }
                countDownTimer.cancel();
                if(isPlay){                                     //for play
                    isPlay = false;
                    textViewStatus.setText("Playing");
                    btnPlay.setBackground(getDrawable(R.drawable.ic_baseline_pause_24));
                    long d = duration-timelineSeconds;
                    countDownTimer = new CountDownTimer((d*1000), 1000) {

                        public void onTick(long millisUntilFinished) {
                            timelineSeconds += 1;
                            textViewSeconds.setText(toSecondsString(timelineSeconds));
                        }
                        public void onFinish() {
                            resetTimeline();
                        }
                    }.start();
                } else {                                         //for pause
                    textViewStatus.setText("Paused");
                    isPlay = true;
                    btnPlay.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));

                }
            }
        });
        
        btnFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = true;
                btnPlay.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
                countDownTimer.cancel();
                long d = duration-timelineSeconds;
                countDownTimer = new CountDownTimer((d*1000)/10, 100) {

                    public void onTick(long millisUntilFinished) {
                        timelineSeconds += 1;
                        textViewStatus.setText("Fast Forwarding");
                        textViewSeconds.setText(toSecondsString(timelineSeconds));
                    }
                    public void onFinish() {
                        resetTimeline();
                    }
                }.start();
            }
        });
    }

    private void resetTimeline() {
        btnPlay.setBackground(getDrawable(R.drawable.ic_baseline_play_arrow_24));
        timelineSeconds = 0;
        textViewSeconds.setText(toSecondsString(timelineSeconds));
        textViewStatus.setText("Stopped");
    }


    private String toSecondsString(long seconds) {
        int hh = (int) (seconds / 3600);
        seconds = seconds % 3600;
        int mm = (int) (seconds / 60);
        int ss = (int) seconds % 60;

        String strHours=(hh<10)?"0"+Integer.toString(hh):Integer.toString(hh);
        String strMin=(mm<10)?"0"+Integer.toString(mm):Integer.toString(mm);
        String strSec=(ss<10)?"0"+Integer.toString(ss):Integer.toString(ss);
        return strHours+":"+strMin+":"+strSec;
    }

}