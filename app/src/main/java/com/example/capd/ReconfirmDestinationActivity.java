package com.example.capd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReconfirmDestinationActivity extends AppCompatActivity {

    Button yes_btn, no_btn;
    TextView question;
    TTS tts;
    String destination;
    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reconfirm_destination);

        yes_btn = (Button)findViewById(R.id.yes_btn);
        no_btn = (Button)findViewById(R.id.no_btn);
        question = (TextView)findViewById(R.id.question);

        Intent intent = new Intent(this.getIntent());
        destination = intent.getStringExtra("destination");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        tts = new TTS(this);

        question.setText("목적지를 " + destination + "로 설정하시겠습니까?");


        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.startTTS(question.getText().toString());
            }
        });


        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){
                    case R.id.yes_btn :
                        changeIntent();
                        break;
                    case R.id.no_btn :
                        finish();
                        break;
                }

            }
        };


        Button.OnLongClickListener onLongClickListener = new Button.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                switch(v.getId()){
                    case R.id.yes_btn :
                        tts.startTTS(yes_btn.getText().toString());
                        break;
                    case R.id.no_btn :
                        tts.startTTS(no_btn.getText().toString());
                        break;
                }
                return true;
            }
        };

        yes_btn.setOnClickListener(onClickListener);
        no_btn.setOnClickListener(onClickListener);

        yes_btn.setOnLongClickListener(onLongClickListener);
        no_btn.setOnLongClickListener(onLongClickListener);

    }


    public void changeIntent(){
        /*
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class); //DestinationSearchActivity는 임의로 지정한 것이므로 추후에 버스리스트 액티비티로 수정 필요
        intent.putExtra("destination", destination);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
        */

        // 정류장 선택 activity 보여주기
        Intent intent = new Intent(this, StationSelectActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onDestroy() {
        tts.close();
        super.onDestroy();
    }
}
