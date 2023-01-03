package master.pro.houssine.pfe.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.Timer;

import master.pro.houssine.pfe.Adapter.ViewAdapter;
import master.pro.houssine.pfe.R;

public class AcceuilActivity extends AppCompatActivity {

    RelativeLayout ll1;
    ViewPager viewPager;
    ImageView dropmenu,backg;
    WormDotsIndicator dot3;
    ViewAdapter viewAdapter;
    FloatingActionButton activer_maps;
    FloatingActionButton statistic;
    ExtendedFloatingActionButton call_police;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    static  final String num_police = "27626528";
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

//        viewPager = findViewById(R.id.view_pager);
        dropmenu = findViewById(R.id.dropdown_menu);
        ll1 = findViewById(R.id.ll1);
        activer_maps = findViewById(R.id.activer_maps);
        call_police = findViewById(R.id.call_protection);
        statistic = findViewById(R.id.statistic);
//        backg = findViewById(R.id.backg);

//        dot3 = findViewById(R.id.dot3);

//        ViewAdapter viewAdapter = new ViewAdapter(AcceuilActivity.this);
//        viewPager.setAdapter(viewAdapter);
//
//        dot3.setViewPager(viewPager);
//
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == 3) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);

        activer_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceuilActivity.this, Maps_Citoyen.class);
                startActivity(intent);
            }
        });


        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcceuilActivity.this, StatistiqueActivity.class);
                startActivity(intent);
            }
        });

        call_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(num_police);
            }
        });

        dropmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AcceuilActivity.this);
                dialog.setContentView(R.layout.popup_login);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.TOP | Gravity.LEFT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                LinearLayout login = dialog.findViewById(R.id.login_page);
                LinearLayout privacy = dialog.findViewById(R.id.privacy);
                LinearLayout aboutApp = dialog.findViewById(R.id.aboutApp);

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AcceuilActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

                privacy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AcceuilActivity.this, regles.class);
                        startActivity(intent);
                    }
                });

                aboutApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AcceuilActivity.this, a_propos.class);
                        startActivity(intent);
                    }
                });

                dialog.show();
            }
        });

    }

    private void makePhoneCall(String num) {

        if(ContextCompat.checkSelfPermission(AcceuilActivity.this,
                android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AcceuilActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else{
            String dial = "tel:" + num;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
}