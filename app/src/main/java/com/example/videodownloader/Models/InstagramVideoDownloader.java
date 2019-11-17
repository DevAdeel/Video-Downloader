package com.example.videodownloader.Models;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.videodownloader.Interfaces.VideoDownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;

public class InstagramVideoDownloader implements VideoDownloader {

    private Context context;
    private String VideoURL;
    private String VideoTitle;

    public InstagramVideoDownloader(Context context, String videoURL) {
        this.context = context;
        VideoURL = videoURL;
    }

    @Override
    public String createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                                       File.separator + "My Video Downloader");

        File subFolder = null;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        else {
            boolean success1 = true;
            subFolder = new File(folder.getPath()+File.separator+"Instagram Videos");
            if(!subFolder.exists())
            {
                success1 = subFolder.mkdirs();
            }
        }
        assert subFolder != null;
        return subFolder.getPath();
    }

    @Override
    public String getVideoId(String link) {
        return link;
    }

    @Override
    public void DownloadVideo() {
        new Data().execute(getVideoId(VideoURL));
    }
    @SuppressLint("StaticFieldLeak")
    private class Data extends AsyncTask<String, String,String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                String buffer = "No URL";
                String Line;
                while ((Line = reader.readLine()) != null)
                {
                    if(Line.contains("og:title")) {
                        VideoTitle = Line.substring(Line.indexOf("og:title"));
                        VideoTitle = Line.substring(Line.indexOf("content"));
                        VideoTitle = VideoTitle.substring(ordinalIndexOf(VideoTitle,"\"",0)+1,ordinalIndexOf(VideoTitle,"\"",1));
                        Log.e("Hello", VideoTitle);
                    }

                    if(Line.contains("og:video"))
                    {
                        Line = Line.substring(Line.indexOf("og:video"));
                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                        if(Line.contains("amp;")) {
                            Line = Line.replace("amp;", "");
                        }
                        if(!Line.contains("https"))
                        {
                            Line = Line.replace("http","https");
                        }
                        Log.e("Hello1",Line);
                        buffer=Line;
                        break;
                    }
                    else {
                        buffer = "No URL";
                    }
                }
                return buffer;
            } catch (IOException e) {
                return "No URL";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.contains("No URL")) {
                String path = createDirectory();
                if(VideoTitle == null || VideoTitle.equals(""))
                {
                    VideoTitle = "InstaVideo" + new Date().toString()+".mp4";
                }
                else {
                    VideoTitle = VideoTitle + ".mp4";
                }
                File newFile = new File(path, VideoTitle);
                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));
                    request.allowScanningByMediaScanner();
                    request.setDescription(VideoTitle)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                            .setDestinationUri(Uri.fromFile(newFile))
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setVisibleInDownloadsUi(true)
                            .setTitle("Downloading");
                    DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    assert manager != null;
                    long downLoadID = manager.enqueue(request);
                } catch (Exception e) {
                    if (Looper.myLooper()==null)
                    Looper.prepare();
                    Toast.makeText(context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
            else {
                if (Looper.myLooper()==null)
                Looper.prepare();
                Toast.makeText(context, "Wrong Video URL or Private Video Can't be downloaded", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }
    private static int ordinalIndexOf(String str, String substr, int n) {
        int pos = -1;
        do {
            pos = str.indexOf(substr, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }
}
