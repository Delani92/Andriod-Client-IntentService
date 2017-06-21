package com.example.delaniperera.blind_app_1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.os.StrictMode;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import java.util.logging.Handler;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;

import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
public class MainActivity extends AppCompatActivity {

    public static String msg_his = "";
    public static String msg_cur = "";
    public static final String message = "";
    public TextView textField;
    private IntentFilter mIntentFilter;
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    private String LOG_TAG = "Delani";
    private Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(message);
        textField = (TextView) findViewById(R.id.textView1);
        textField.setMovementMethod(new ScrollingMovementMethod());
        Intent serviceIntent = new Intent(this, Blind_Service.class);
        startService(serviceIntent);
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, serviceIntent, 0);

       // Vibrator vibrator = (Vibrator) Contex.getSystemService(Context.VIBRATOR_SERVICE);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);








                    }

    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }



    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
         //  String msg = intent.getStringExtra(message);


            //textField.setText(textField.getText());
                  //  + "Broadcast From Service: \n");
            if (intent.getAction().equals(message)) {

                msg_cur =intent.getStringExtra("Data");
                msg_his= msg_his+ msg_cur +"\n\n";


                Log.d(LOG_TAG, " message : " + msg_his);
                textField.setText(msg_his + "\n\n");

                String str = intent.getStringExtra("Data").toString();
                boolean correct = str.contentEquals("1 ");
               // boolean correct = "1 ".equals(str);
                Log.d(LOG_TAG, " boool.... : " + correct);
                try {
                notification.setSmallIcon(R.drawable.ic_3);
                  //  notification.setTicker("");
                notification.setTicker(msg_cur);

                   //Notification n_1 = new Notification();
                   //n_1.flags|=Notification.FLAG_ONLY_ALERT_ONCE;
               notification.setWhen(System.currentTimeMillis());
               notification.setContentTitle(msg_cur);

                if(correct == true) {
                   // vibrator.vibrate(500);
                    Log.d(LOG_TAG, "true....xxxx : ");
                    long[] pattern = {
                            20,   //Off before vibration
                            500, 500,  //on-off
                            500, 500, //on-off
                    };
                    notification.setVibrate(pattern);

                    //Uri path_2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vibrate_sound_trimmed);
                   // Uri path_2 = Uri.parse("android.resource://com.example.delaniperera.blind_app_1." + R.raw.vibration_message);
                    //notification.setSound(path_2);


                }


                //notification.setContentText("I am the body text of your notification");

                //PendingIntent pendingIntent = PendingIntent.getActivity(intent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //notification.setContentIntent(pendingIntent);

                //Builds notification and issues it
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(uniqueID, notification.build());

                   // nm.cancel(uniqueID);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
                else {
                Intent stopIntent = new Intent(MainActivity.this,Blind_Service.class);
                stopService(stopIntent);
                }

            }
        };
    protected void onPause() {
        unregisterReceiver(mReceiver);
        //registerReceiver(mReceiver, mIntentFilter);
        super.onPause();
    }


}
