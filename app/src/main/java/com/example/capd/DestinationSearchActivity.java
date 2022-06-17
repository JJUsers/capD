package com.example.capd;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;

// 목적지 검색 액티비티

public class DestinationSearchActivity extends AppCompatActivity {

    Button search_text_btn;
    Button search_voice_btn;
    EditText edit_destination;
    String destination;
    Intent intent;
    SpeechRecognizer speechRecognizer;
    final int PERMISSION = 1;
    TTS tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_search);

        search_text_btn = (Button) findViewById(R.id.search_text_btn);
        search_voice_btn = (Button) findViewById(R.id.search_voice_btn);
        edit_destination = (EditText) findViewById(R.id.edit_destination);



        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        tts = new TTS(this);

        //음성 인식을 위해 intent 객체 생성
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");


        Button.OnClickListener onClickListener = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){
                    case R.id.search_text_btn :
                        searchByText();
                        break;
                    case R.id.search_voice_btn :
                        searchByVoice();
                        break;
                }

            }
        };

        Button.OnLongClickListener onLongClickListener = new Button.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                switch(v.getId()){
                    case R.id.search_text_btn :
                        tts.startTTS(search_text_btn.getText().toString());
                        break;
                    case R.id.search_voice_btn :
                        tts.startTTS(search_voice_btn.getText().toString());
                        break;
                }
                return true;
            }
        };



        search_text_btn.setOnClickListener(onClickListener);
        search_voice_btn.setOnClickListener(onClickListener);

        search_text_btn.setOnLongClickListener(onLongClickListener);
        search_voice_btn.setOnLongClickListener(onLongClickListener);


    }

    //텍스트로 검색
    public void searchByText(){

        destination = edit_destination.getText().toString().trim();
        if (destination == null || destination.isEmpty()){
            tts.startTTS("목적지를 입력해주세요");
        }else{
            nextActivity();
        }

    }

    //음성으로 검색
    public void searchByVoice(){

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(DestinationSearchActivity.this);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);
    }



    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "목적지를 말씀해주세요", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 오류";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 오류";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트워크 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없습니다";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁩니다";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버 오류";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "음성 인식 시간 초과";
                    break;
                default:
                    message = "알 수 없는 오류 발생";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            destination = matches.iterator().next();
            nextActivity();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };


    public void nextActivity(){
        Intent intent2 = new Intent(DestinationSearchActivity.this, DestinationListActivity.class);
        intent2.putExtra("destination", destination);
        startActivity(intent2);
    }

    @Override
    protected void onDestroy() {
        tts.close();
        super.onDestroy();
    }


}

