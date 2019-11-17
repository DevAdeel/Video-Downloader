package com.example.videodownloader.Models;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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

public class DailyMotionDownloader {

    private Context context;
    private String Quality;
    private String FinalURL;
    private String VideoURL;
    private long DownLoadID;

    public DailyMotionDownloader(Context context, String quality,String videoURL,int downloadManagerID) {
        this.context = context;
        Quality = quality;
        VideoURL =videoURL;
        DownLoadID = downloadManagerID;
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
            subFolder = new File(folder.getPath()+File.separator+"Dailymotion Videos");
            if(!subFolder.exists())
            {
                success1 = subFolder.mkdirs();
            }
        }
        return subFolder.getPath();
    }

    private String getVideoId(String link) {
        String videoId;
        if (link.contains("?")) {
             videoId= link.substring(link.indexOf("video/") + 1, link.indexOf("?"));
        }
        else {
            videoId = link.substring(link.indexOf("video/") + 1);
        }
        videoId = videoId.substring(videoId.lastIndexOf("/")+1);
        return videoId;
    }
    private class Data extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String...parm) {
            BufferedReader reader = null;
            try {
                URL url = new URL("https://www.dailymotion.com/embed/video/" + parm[0] +"?autoplay=1?__a=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String Line = "";
                while ((Line = reader.readLine()) != null)
                {
                    if(Line.contains("var config")) {
                        String line2 = Line.substring(Line.lastIndexOf("qualities")+1);
                        switch (Quality) {
                            case "144p": {
                                if (line2.contains("144")) {
                                    String line3 = line2.substring(line2.indexOf("144"), line2.indexOf("240"));
                                    String line4 = line3.substring(line3.indexOf("video\\/mp4\""), line3.lastIndexOf("}"));
                                    String UrlFinal = line4.substring(line4.indexOf("https"), line4.lastIndexOf("\""));
                                    buffer.append(UrlFinal);
                                }
                                else {
                                    Toast.makeText(context, "Video Quality not available, Select another", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case "240p": {
                                if (line2.contains("240")) {
                                    String line3 = line2.substring(line2.indexOf("240"), line2.indexOf("380"));
                                    String line4 = line3.substring(line3.indexOf("video\\/mp4\""), line3.lastIndexOf("}"));
                                    String UrlFinal = line4.substring(line4.indexOf("https"), line4.lastIndexOf("\""));
                                    buffer.append(UrlFinal);
                                }
                                else {
                                    Toast.makeText(context, "Video Quality not available, Select another", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case "380p":{
                                if (line2.contains("380")) {
                                    String line3 = line2.substring(line2.indexOf("380"), line2.indexOf("480"));
                                    String line4 = line3.substring(line3.indexOf("video\\/mp4\""), line3.lastIndexOf("}"));
                                    String UrlFinal = line4.substring(line4.indexOf("https"), line4.lastIndexOf("\""));
                                    buffer.append(UrlFinal);
                                }
                                else {
                                    Toast.makeText(context, "Video Quality not available, Select another", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case "480p":{
                                if (line2.contains("480")) {
                                    String line3 = line2.substring(line2.indexOf("480"), line2.indexOf("720"));
                                    String line4 = line3.substring(line3.indexOf("video\\/mp4\""), line3.lastIndexOf("}"));
                                    String UrlFinal = line4.substring(line4.indexOf("https"), line4.lastIndexOf("\""));
                                    buffer.append(UrlFinal);
                                }
                                else {
                                    Toast.makeText(context, "Video Quality not available, Select another", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case "720p":{
                                if (line2.contains("720")) {
                                    String line3 = line2.substring(line2.indexOf("720"), line2.indexOf("1080"));
                                    String line4 = line3.substring(line3.indexOf("video\\/mp4\""), line3.lastIndexOf("}"));
                                    String UrlFinal = line4.substring(line4.indexOf("https"), line4.lastIndexOf("\""));
                                    buffer.append(UrlFinal);
                                }
                                else {
                                    Toast.makeText(context, "Video Quality not available, Select another", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }
                }
                return buffer.toString();
                //Data.execute();
                //Log.e("Hi",document.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            FinalURL = o;
            String path = createDirectory();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm");
            File newFile = new File(path, simpleDateFormat.format(new Date()) + "video.mp4");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(FinalURL));
            request.allowScanningByMediaScanner();
            request.setDescription("Video Downloading")
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                    .setDestinationUri(Uri.fromFile(newFile))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setVisibleInDownloadsUi(true)
                    .setTitle("Downloading");
            DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            DownLoadID = manager.enqueue(request);
        }
    }
}
