package com.example.delaniperera.blind_app_1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by DelaniPerera on 6/8/17.
 */

public class Blind_Service extends Service {
    private String LOG_TAG = "Delani";
    Socket client;
    String received_message;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "In onBind");
        return null;
    }


    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "In onStartCommand");
        try {
            client = new Socket("192.168.43.126", 9090);
            client.setKeepAlive(true);
            Log.i(LOG_TAG, "Client Connected");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    while(client.getInputStream()!=null) {
                        // for(int i=0; i<client.getInputStream().available(); i++){
                        //    Log.d(LOG_TAG," message available");
                        //}
                        // client.getInputStream().available();
                        InputStream is = client.getInputStream();

                        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(is));

                        received_message = receiveRead.readLine().toString();
                        receiveRead.ready();
                        //received_message = received_message+received_message;
                        Log.d(LOG_TAG, " message from service : " + received_message);
                       // received_message = received_message+received_message;
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(MainActivity.message);
                        broadcastIntent.putExtra("Data",received_message);
                        sendBroadcast(broadcastIntent);


                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        return START_REDELIVER_INTENT;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

}
