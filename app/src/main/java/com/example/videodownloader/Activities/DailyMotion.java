package com.example.videodownloader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.videodownloader.Models.DailyMotionDownloader;
import com.example.videodownloader.R;

public class DailyMotion extends AppCompatActivity {

    private EditText txt_getURL;
    private RadioGroup radioGroup;
    private RadioButton btn =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymotion);

        txt_getURL = findViewById(R.id.input_DailymotionURL);
        radioGroup = findViewById(R.id.radiogroup_dailymotion);
        Button btn_Download = findViewById(R.id.btn_downloadDailymotion);

        btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyMotionDownloader downloader = new DailyMotionDownloader(DailyMotion.this,getVideoQuality(),txt_getURL.getText().toString(),12);
                downloader.DownloadVideo();
            }
        });

    }

    private String getVideoQuality()
    {
        if(radioGroup.getCheckedRadioButtonId() != -1)
        {
            btn = findViewById(radioGroup.getCheckedRadioButtonId());
        }
        return btn.getText().toString();
    }
}
