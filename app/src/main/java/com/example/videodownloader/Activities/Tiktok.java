package com.example.videodownloader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.videodownloader.Models.TiktokVideoDownloader;
import com.example.videodownloader.R;

public class Tiktok extends AppCompatActivity {

    EditText inputURl;
    Button BtnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktok);

        inputURl = findViewById(R.id.input_TiktokURL);
        BtnDownload = findViewById(R.id.btn_downloadTiktokVideo);

        BtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL = inputURl.getText().toString();
                TiktokVideoDownloader downloader = new TiktokVideoDownloader(Tiktok.this, URL);
                downloader.DownloadVideo();
            }
        });
    }
}
