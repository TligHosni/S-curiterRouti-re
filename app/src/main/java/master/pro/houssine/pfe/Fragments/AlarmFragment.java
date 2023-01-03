package master.pro.houssine.pfe.Fragments;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import master.pro.houssine.pfe.Activities.MyAlarmReceiver;
import master.pro.houssine.pfe.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmFragment#} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment {

    static EditText duree;
    static EditText alarm_text;
    Button send_alarm;
    DrawerLayout drawerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        duree = view.findViewById(R.id.duree);
        alarm_text = view.findViewById(R.id.alarm_text);
        send_alarm = view.findViewById(R.id.send_alarm);

        send_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alarm = alarm_text.getText().toString();
                startAlarm(getActivity(), alarm);
            }
        });


        return view;
    }


    public static void startAlarm(Context context, String alarm) {

        Intent intent = new Intent(context, MyAlarmReceiver.class);

        MyAlarmReceiver myAlarmReceiver = new MyAlarmReceiver();
        MyAlarmReceiver.setAlarm_text(alarm);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(

                context.getApplicationContext(), 0 , intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis()

                        + ( Integer.parseInt(duree.getText().toString()) * 1000L)), pendingIntent); //10 seconds interval

    }

//    public void ClickLogo(View view) {
//
//        MainActivity.closeDrawer(drawerLayout);
//    }
}