package com.example.videodownloader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.videodownloader.Models.TwitterVideoDownloader;
import com.example.videodownloader.R;

public class Twitter extends AppCompatActivity {

    EditText inputURl;
    Button BtnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        inputURl = findViewById(R.id.input_TwitterURL);
        BtnDownload = findViewById(R.id.btn_downloadTwitterVideo);

        BtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL = inputURl.getText().toString();
                TwitterVideoDownloader downloader = new TwitterVideoDownloader(Twitter.this, URL);
                downloader.DownloadVideo();
            }
        });
    }
}
