package com.example.capd;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn_destination_search;
    Button btn_voice_speed_control;
    TTS tts;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        btn_destination_search = (Button)findViewById(R.id.btn_destination_search);
        btn_voice_speed_control = (Button)findViewById(R.id.btn_voice_speed_control);

        tts = new TTS(this);


        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){
                    case R.id.btn_destination_search :
                        intent = new Intent(getApplicationContext(), DestinationSearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_voice_speed_control :
                        intent = new Intent(getApplicationContext(), VoiceSpeedSettingActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        };

        Button.OnLongClickListener onLongClickListener = new Button.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                switch(v.getId()){
                    case R.id.btn_destination_search :
                        tts.startTTS(btn_destination_search.getText().toString());
                        break;
                    case R.id.btn_voice_speed_control :
                        tts.startTTS(btn_voice_speed_control.getText().toString());
                        break;
                }
                return true;
            }
        };



        btn_destination_search.setOnClickListener(onClickListener);
        btn_voice_speed_control.setOnClickListener(onClickListener);

        btn_destination_search.setOnLongClickListener(onLongClickListener);
        btn_voice_speed_control.setOnLongClickListener(onLongClickListener);
    }


    @Override
    protected void onDestroy() {
        tts.close();
        super.onDestroy();
    }
}
