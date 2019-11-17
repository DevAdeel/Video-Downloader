package com.example.videodownloader.Models;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;

public class  TopBuzzDownloader {

    private Context context;
    private String FinalURL;
    private String VideoURL;
    private long DownLoadID;
    private String VideoTitle;

    public TopBuzzDownloader(Context context, String videoURL, long downLoadID) {
        this.context = context;
        VideoURL = videoURL;
        DownLoadID = downLoadID;
    }

    public void DownloadVideo(){
        new Data().execute(getVideoId(VideoURL));
    }
    private String createDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                                       File.separator + "My Video Downloader");

        File subFolder = null;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        else {
            boolean success1 = true;
            subFolder = new File(folder.getPath()+File.separator+"TopBuzz Videos");
            if(!subFolder.exists())
            {
                success1 = subFolder.mkdirs();
            }
        }
        return subFolder.getPath();
    }

    private String getVideoId(String link) {
        return link;
    }
    private class Data extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String...parm) {
            BufferedReader reader = null;
            try {
                URL url = new URL(parm[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String Line = "";
                while ((Line = reader.readLine()) != null)
                {
                    if(Line.contains("INITIAL_STATE")) {
                        if(Line.contains("title"))
                        {
                            Line = Line.substring(Line.indexOf("videoTitle"));
                            VideoTitle = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\\",2));
                            if(Line.contains("480p"))
                            {
                                Line = Line.substring(Line.indexOf("480p"));
                                Line = Line.substring(Line.indexOf("main_url"));
                                Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                                Line = Line.replace("u002F","");
                                if(Line.contains("http")) {
                                    Line = Line.replace("http", "https");
                                }
                                buffer.append(Line);
                            }
                            else {
                                buffer.append("No URL");
                            }
                            /*switch (Quality)
                            {
                                case "480p":
                                {
                                    if(Line.contains("480p"))
                                    {
                                        Line = Line.substring(Line.indexOf("480p"));
                                        Line = Line.substring(Line.indexOf("main_url"));
                                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                                        Line = Line.replace("u002F","");
                                        if(Line.contains("http")) {
                                            Line = Line.replace("http", "https");
                                        }
                                        buffer.append(Line);
                                    }
                                    else {
                                        buffer.append("No URL");
                                    }
                                }
                                case "240p":
                                {
                                    if(Line.contains("240p"))
                                    {
                                        Line = Line.substring(Line.indexOf("240p"));
                                        Line = Line.substring(Line.indexOf("main_url"));
                                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                                        Line = Line.replace("u002F","");
                                        if(Line.contains("http")) {
                                            Line = Line.replace("http", "https");
                                        }
                                        buffer.append(Line);
                                    }
                                    else {
                                        buffer.append("No URL");
                                    }
                                }
                                case "720p":
                                {
                                    if(Line.contains("720p"))
                                    {
                                        Line = Line.substring(Line.indexOf("720p"));
                                        Line = Line.substring(Line.indexOf("main_url"));
                                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                                        Line = Line.replace("u002F","");
                                        if(Line.contains("http")) {
                                            Line = Line.replace("http", "https");
                                        }
                                        buffer.append(Line);
                                    }
                                    else {
                                        buffer.append("No URL");
                                    }
                                }
                                case "144p":
                                {
                                    if(Line.contains("144p"))
                                    {
                                        Line = Line.substring(Line.indexOf("144p"));
                                        Line = Line.substring(Line.indexOf("main_url"));
                                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                                        Line = Line.replace("u002F","");
                                        if(Line.contains("http")) {
                                            Line = Line.replace("http", "https");
                                        }
                                        buffer.append(Line);
                                    }
                                    else {
                                        buffer.append("No URL");
                                    }
                                }
                                case "380p":
                                {
                                    if(Line.contains("380p"))
                                    {
                                        Line = Line.substring(Line.indexOf("380p"));
                                        Line = Line.substring(Line.indexOf("main_url"));
                                        Line = Line.substring(ordinalIndexOf(Line,"\"",1)+1,ordinalIndexOf(Line,"\"",2));
                                        Line = Line.replace("u002F","");
                                        if(Line.contains("http")) {
                                            Line = Line.replace("http", "https");
                                        }
                                        buffer.append(Line);
                                    }
                                    else {
                                        buffer.append("No URL");
                                    }
                                }
                            }*/

                        }
                    }
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            FinalURL = o;
            if(FinalURL != null || FinalURL != "No URL") {
                String path = createDirectory();
                File newFile = new File(path, VideoTitle);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(FinalURL));
                request.allowScanningByMediaScanner();
                request.setDescription(VideoTitle)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        .setDestinationUri(Uri.fromFile(newFile))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setVisibleInDownloadsUi(true)
                        .setTitle("Downloading");
                DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                DownLoadID = manager.enqueue(request);
            }
            else {
                Toast.makeText(context, "Video Can't be downloaded!", Toast.LENGTH_SHORT).show();
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
