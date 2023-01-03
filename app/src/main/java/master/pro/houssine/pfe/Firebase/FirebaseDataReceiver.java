package master.pro.houssine.pfe.Firebase;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import master.pro.houssine.pfe.Activities.MainActivity;
import master.pro.houssine.pfe.R;

public class FirebaseDataReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "10002";
    public static int NbrNotifDiscussion;
    private final String TAG = "FirebaseDataReceiver";
    private final String CHANNEL_ID = "001";
    Timer timer;


    //*********************************************************************************************//
    TimerTask doTask;

    private NotificationCompat.Builder mBuilder;
    private LocalBroadcastManager broadcaster;
    private String type;

    @Override
    public void onReceive(Context context, Intent intent) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        Bundle bundle = intent.getExtras();
        broadcaster = LocalBroadcastManager.getInstance(context);

        if (bundle != null) {

            String title = bundle.getString("title");
            String type = bundle.getString("type");
            String body = bundle.getString("content");
            String id = bundle.getString("element");
            String user_id = bundle.getString("user_id");

            if (body != null && !body.equals("")) {

                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.putExtra("name", false);
                    intent1.putExtra("from", "dataReceiver");
                    intent1.putExtra("type", type);
                    intent1.putExtra("element", id);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent;
                    intent1.setAction(Long.toString(System.currentTimeMillis()));
                    pendingIntent = PendingIntent.getActivity(context, m, intent1, PendingIntent.FLAG_ONE_SHOT);
                    PendingIntent dismissIntent = NotificationActivity.getDismissIntent(0, context.getApplicationContext());

                //   display notification in status bar
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};

                mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.accidentlocation);
                mBuilder.setContentTitle(title)
                        .setColor(context.getResources().getColor(R.color.colorAccent))
                        .setVibrate(pattern)
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.check_box, "Confirme", pendingIntent)
                        .addAction(R.drawable.close_option, "Annuler", dismissIntent)
                        .setDefaults(Notification.DEFAULT_ALL)//
                        .setStyle(new NotificationCompat.InboxStyle())///
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE) ///
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri);

               NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
               mNotificationManager.cancelAll();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.YELLOW);
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(notificationChannel);

                }

                if (id != null) {
                    mNotificationManager.notify(Integer.parseInt(id), mBuilder.build());
                } else {
                    mNotificationManager.notify(m, mBuilder.build());
                }


            }

        }


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(Integer.parseInt(NOTIFICATION_CHANNEL_ID));


    }



}