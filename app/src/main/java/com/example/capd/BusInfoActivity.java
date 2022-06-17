package com.example.capd;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BusInfoActivity extends AppCompatActivity {
    private static final String TAG = "capD";

    private TextToSpeech textToSpeech;          // TTS
    private boolean isSpeechOn;                 // TTS 준비상태

    private String speechText;                  // TTS 에 사용할 text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_info_activity);

        // 출발정류장, 도착정류장
        Intent intent = getIntent();
        String via = intent.getStringExtra("via");
        String busNo = intent.getStringExtra("bus_no");
        String rTime = intent.getStringExtra("r_time");
        String rStop = intent.getStringExtra("r_stop");

        setTitle(R.string.title_bus_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView) findViewById(R.id.txtVia)).setText(via);        // 방면명
        ((TextView) findViewById(R.id.txtBusNo)).setText(busNo);    // 버스번호
        ((TextView) findViewById(R.id.txtRTime)).setText(rTime);    // 남은 정류장수
        ((TextView) findViewById(R.id.txtRStop)).setText(rStop);    // 도착 예정시간

        findViewById(R.id.btnRiding).setOnClickListener(view -> {
            // 탑승

        });

        findViewById(R.id.btnBoard).setOnClickListener(view -> {
            // 전광판

        });

        findViewById(R.id.btnBack).setOnClickListener(view -> {
            // 뒤로가기
            finish();
        });

        // TTS 에 사용할 text 구성
        this.speechText = busNo + " " + rTime + " " + rStop;

        // TTS 초기화
        initTextToSpeech();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.textToSpeech != null) {
            this.textToSpeech.stop();
            this.textToSpeech.shutdown();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* TTS 초기화 */
    private void initTextToSpeech() {
        this.textToSpeech = new TextToSpeech(this, state -> {
            if (state == TextToSpeech.SUCCESS) {
                // 한국어로 설정
                int result = this.textToSpeech.setLanguage(Locale.KOREAN);
                //int result = this.textToSpeech.setLanguage(Locale.ENGLISH);

                // Language 지원하지 않음
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.d(TAG, "Language is not supported");
                    this.isSpeechOn = false;
                } else {
                    this.isSpeechOn = true;
                }
            } else {
                Log.d(TAG, "오류:" + state);
                this.isSpeechOn = false;
            }

            if (this.isSpeechOn) {
                // 텍스트를 음성으로
                speechText(this.speechText);
            }
        });

        // 합성된 음성 데이터 Queue 로부터 텍스트 읽기를 처리할 때 사용하는 리스너.
        // TextToSpeech 의 대표적인 메서드인 speak()나 synthesizeToFile()은 비동기로 동작하기 때문에 완료, 오류 발생에 대한 처리를 하고 싶다면 이 리스너를 사용해야 한다
        this.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                Log.d(TAG, "onStart:" + s);
            }

            @Override
            public void onDone(String s) {
                Log.d(TAG, "onDone:" + s);
            }

            @Override
            public void onError(String s) {
                Log.d(TAG, "onError:" + s);
            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
            }
        });
    }

    /* 텍스트를 음성으로 */
    private void speechText(String text) {
        if (TextUtils.isEmpty(text)) {
            Log.d(TAG, "not speech text");
            return;
        }

        // QUEUE_ADD : 재생 대기열 끝에 새 항목이 추가되는 대기열 모드입니다.
        // QUEUE_FLUSH : 재생 대기열의 모든 항목 (재생할 미디어 및 합성 할 텍스트)이 삭제되고 새 항목으로 대체되는 대기열 모드입니다. 주어진 호출 앱과 관련하여 큐가 플러시됩니다. 다른 수신자의 대기열에있는 항목은 삭제되지 않습니다.
        // utteranceId : 이 요청의 고유 식별자입니다.

        //this.textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, params, "id");
        this.textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, "id");
    }
}
