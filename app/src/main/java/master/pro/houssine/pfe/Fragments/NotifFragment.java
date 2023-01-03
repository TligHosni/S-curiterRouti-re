package master.pro.houssine.pfe.Fragments;

import static master.pro.houssine.pfe.Utils.AppUtils.CHANNEL_ID;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import master.pro.houssine.pfe.Activities.AppFragment;
import master.pro.houssine.pfe.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifFragment#} factory method to
 * create an instance of this fragment.
 */
public class NotifFragment extends Fragment {

    DrawerLayout drawerLayout;
    EditText notif_title;
    EditText notif_details;
    Button send_notif;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notif_title = view.findViewById(R.id.notif_title);
        notif_details = view.findViewById(R.id.notif_details);
        send_notif = view.findViewById(R.id.send_notif);

        send_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = notif_title.getText().toString();
                String details = notif_details.getText().toString();

                displayNotification(getActivity(),title,details);
            }
        });

        return view;
    }

    public static void displayNotification(Activity activity, String title, String details) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity, CHANNEL_ID)
                        .setSmallIcon(R.drawable.t)
                        .setContentTitle(title)
                        .setContentText(details)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        AppCompatActivity appCompatActivity = (AppCompatActivity) activity ;
//        ContactFragment fragment = new ContactFragment();
//        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
//

        Intent intent = new Intent(activity, AppFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(activity);

        mNotificationMgr.notify(1, mBuilder.build());
    }


//    public void ClickLogo(View view) {
//
//        MainActivity.closeDrawer(drawerLayout);
//    }
}