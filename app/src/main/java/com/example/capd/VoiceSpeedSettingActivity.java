package com.example.capd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

//음성 속도 조절
public class VoiceSpeedSettingActivity extends AppCompatActivity {

    float speed;
    float now_speed;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button btn_save;
    Button btn_speed_up;
    Button btn_speed_down;
    TextView tv_speed;
    TTS tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_speed_setting);

        btn_save = (Button)findViewById(R.id.btn_save);
        btn_speed_up = (Button)findViewById(R.id.btn_speed_up);
        btn_speed_down = (Button)findViewById(R.id.btn_speed_down);
        tv_speed = (TextView)findViewById(R.id.tv_speed);


        preferences = getSharedPreferences("speed", Activity.MODE_PRIVATE);

        //저장되어 있는 값 불러오기
        speed = preferences.getFloat("speed_number", 1.0f);
        tv_speed.setText(String.valueOf(speed));


        tts = new TTS(getApplicationContext());


        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){
                    case R.id.btn_speed_up :
                        voiceSpeedUp();
                        break;
                    case R.id.btn_speed_down :
                        voiceSpeedDown();
                        break;
                    case R.id.btn_save :
                        saveSpeed();
                        finish();
                        break;
                }

            }
        };

        Button.OnLongClickListener onLongClickListener = new Button.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                switch(v.getId()){
                    case R.id.btn_speed_up :
                        tts.startTTS(btn_speed_up.getText().toString());
                        break;
                    case R.id.btn_speed_down :
                        tts.startTTS(btn_speed_down.getText().toString());
                        break;
                    case R.id.btn_save :
                        tts.startTTS(btn_save.getText().toString());
                        break;
                }
                return true;
            }
        };

        btn_speed_up.setOnClickListener(onClickListener);
        btn_speed_down.setOnClickListener(onClickListener);
        btn_save.setOnClickListener(onClickListener);

        btn_speed_up.setOnLongClickListener(onLongClickListener);
        btn_speed_down.setOnLongClickListener(onLongClickListener);
        btn_save.setOnLongClickListener(onLongClickListener);

    }

    //음성 속도 올리기
    public void voiceSpeedUp(){

        String sup = tv_speed.getText().toString();
        BigDecimal now_speed_BD = new BigDecimal(sup);
        BigDecimal plus_speed = new BigDecimal("0.5");
        now_speed = now_speed_BD.floatValue();

        if (now_speed > 10.0){
            btn_speed_up.setEnabled(false);
        }else {
            btn_speed_up.setEnabled(true);
        }

        BigDecimal update_speed = now_speed_BD.add(plus_speed);
        float plus_result = update_speed.floatValue();
        tv_speed.setText(update_speed.toString());

        tts.startTTS(update_speed + " 속도입니다", plus_result, 1.0f, false);
    }


    //음성속도 내리기
    public void voiceSpeedDown(){

        String sdown = tv_speed.getText().toString();
        BigDecimal now_speed_BD = new BigDecimal(sdown);
        BigDecimal minus_speed = new BigDecimal("0.5");
        now_speed = now_speed_BD.floatValue();

        if (now_speed < 0.1){
            btn_speed_up.setEnabled(false);
        }else {
            btn_speed_up.setEnabled(true);
        }

        BigDecimal update_speed = now_speed_BD.subtract(minus_speed);
        float minus_result = update_speed.floatValue();
        tv_speed.setText(update_speed.toString());

        tts.startTTS(update_speed + " 속도입니다", minus_result, 1.0f, false);
    }


    //음성 속도 저장
    public void saveSpeed(){

        float update_speed = Float.valueOf(tv_speed.getText().toString());
        editor = preferences.edit();
        editor.putFloat("speed_number", update_speed);
        editor.commit();
    }


    @Override
    protected void onDestroy() {
        tts.close();
        super.onDestroy();
    }
}


