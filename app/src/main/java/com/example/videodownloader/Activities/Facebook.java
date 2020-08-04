package com.example.videodownloader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.videodownloader.Models.FbVideoDownloader;
import com.example.videodownloader.R;

public class Facebook extends AppCompatActivity {

    EditText inputURl;
    Button BtnDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        inputURl = findViewById(R.id.input_FbURL);
        BtnDownload = findViewById(R.id.btn_downloadFbVideo);

        BtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL = inputURl.getText().toString();
                FbVideoDownloader downloader = new FbVideoDownloader(Facebook.this,URL);
                downloader.DownloadVideo();
            }
        });
    }
}
