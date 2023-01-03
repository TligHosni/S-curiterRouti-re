package master.pro.houssine.pfe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.Model.User;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Utils.AppUtils;

public class splachscreen extends AppCompatActivity {
    private ProgressBar pgsBar;
    private int i = 0;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splachscreen);
        pgsBar = (ProgressBar) findViewById(R.id.progressBar);
        Thread thread = new Thread(new Runnable()  {

            @Override
            public void run() {

                // Do something ... (Update database,..)
                SystemClock.sleep(5000); // Sleep 5 seconds.

                pgsBar.setIndeterminate(true);
                pgsBar.setMax(1);
                pgsBar.setProgress(1);

            }
        });
        thread.start();


        AppUtils.getToken(getApplicationContext());
        User user = SharedPrefManger.getInstance(this).getUser();

        Thread splashScreenStarter = new Thread() {
            public void run() {
                try {
                    int delay = 0;
                    while (delay < 2000) {
                        sleep(200);
                        delay = delay + 100;
                    }

//                    startActivity(new Intent(splachscreen.this, MainActivity.class));
                    String phone = user.getPhone();

                    if(phone != null) {

                        startActivity(new Intent(splachscreen.this, MainActivity.class));

                    }else{

                        startActivity(new Intent(splachscreen.this, LoginActivity.class));

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }

        };
        splashScreenStarter.start();

    }

}