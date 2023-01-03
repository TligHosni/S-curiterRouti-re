package master.pro.houssine.pfe.Services;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import master.pro.houssine.pfe.Activities.Maps_Citoyen;
import master.pro.houssine.pfe.R;

public class MusicService extends Service {

        MyReceiver recev;
        private MediaPlayer player;


        public MusicService(){}
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        public void onCreate() {

            recev=new MyReceiver();
            registerReceiver(recev,new IntentFilter("playpause"));
            player=MediaPlayer.create(this, R.raw.alarm2);
            super.onCreate();
        }
        @Override
        public int onStartCommand(Intent startIntent, int flags, int startId) {

//intent du clique notif
            Intent notificationIntent = new Intent(this, Maps_Citoyen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
// intent du clique bouton play/pause
            PendingIntent pPPendingIntent = PendingIntent.getBroadcast(this, 0, new
                    Intent("PlayPause"), PendingIntent.FLAG_UPDATE_CURRENT);

// Notification Channel
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            String channelId = "my_channel_id";
            CharSequence channelName = "My Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new
                        NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(notificationChannel);}
            Notification notification =
                    new NotificationCompat.Builder(this, channelId)
                            .setContentTitle("Lecture en cours")
                            .setContentText("Tahir ve Nafess")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .addAction(R.drawable.ic_launcher_foreground, "Play/Pause", pPPendingIntent)
                            .setContentIntent(pendingIntent)
// .setPriority(Notification.PRIORITY_MAX)
                            .build();
            startForeground(110, notification);player.start();return START_STICKY;}

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (player.isPlaying()) player.stop();
            unregisterReceiver(recev);
        }



        public class MyReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action.equals("PlayPause")) {
                    if(player.isPlaying()) {player.pause();}
                    else {player.start();}
                }
            }
        }
}
