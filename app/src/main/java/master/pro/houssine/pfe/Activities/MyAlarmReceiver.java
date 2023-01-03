package master.pro.houssine.pfe.Activities;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyAlarmReceiver extends BroadcastReceiver {

  static String alarm_text;

    public static String getAlarm_text() {
        return alarm_text;
    }

    public static void setAlarm_text(String alarm_text) {
        MyAlarmReceiver.alarm_text = alarm_text;
    }

    public MyAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, alarm_text, Toast.LENGTH_SHORT).show();


    }


//    private void showDialog(MyAlarmReceiver myAlarmReceiver) {
//
//        final Dialog dialog = new Dialog(myAlarmReceiver);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.popup_verify_email);
//
//        dialog.show();
//
//    }

}