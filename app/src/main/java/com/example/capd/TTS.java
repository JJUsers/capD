package com.example.capd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

// text to speech (텍스트 음성 변환)

public class TTS {

    float speed;
    SharedPreferences preferences;


    public Context mContext;
    public TextToSpeech tts;

    // tts 생성
    public TTS(Context context){
        this.mContext = context;
        initTTS(this.mContext);
        preferences = context.getSharedPreferences("speed", Activity.MODE_PRIVATE);
        speed = preferences.getFloat("speed_number", 1.0f);
    }


    // tts 초기화
    public void initTTS(Context context){

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage(Locale.KOREAN);
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "지원되지 않는 언어");
                    }
                }
            }
        });
    }

    public void startTTS(String text){
        startTTS(text, speed, 1.0f, false);
    }

    // tts 실행
    public void startTTS(String text, float speed, float pitch, Boolean is_auto_close){

        if (tts==null){
            initTTS(this.mContext);
        }

        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

        if (is_auto_close){
            close();
        }
    }

    // tts 종료
    public void close() {
        if (tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }


}
