package com.example.videodownloader.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.videodownloader.Models.VimeoVideoDownloader;
import com.example.videodownloader.R;

public class Vimeo extends AppCompatActivity {

    EditText inputURl;
    Button BtnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo);

        inputURl = findViewById(R.id.input_VimeoURL);
        BtnDownload = findViewById(R.id.btn_downloadVimeoVideo);

        BtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String URL = inputURl.getText().toString();
                VimeoVideoDownloader downloader = new VimeoVideoDownloader(Vimeo.this, URL);
                downloader.DownloadVideo();
            }
        });
    }
}
