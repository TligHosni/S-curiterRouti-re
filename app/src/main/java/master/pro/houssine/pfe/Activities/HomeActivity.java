package master.pro.houssine.pfe.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import master.pro.houssine.pfe.R;

public class HomeActivity extends AppCompatActivity {

    TextView text_phone ;
    TextView text_password;
    Button logout;
    DrawerLayout drawerLayout;
    SharedPreferences sharedPreferences;
    Button getData;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_PHONE = "phone1";
    private static final String KEY_PASSWORD = "password1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        text_phone = findViewById(R.id.phone);
        text_password = findViewById(R.id.password);

        drawerLayout = findViewById(R.id.drawer_layout);


        
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String phone1 = sharedPreferences.getString(KEY_PHONE, null);
        String password1 = sharedPreferences.getString(KEY_PASSWORD, null);

        if (phone1 != null || password1 != null)
        {
            text_phone.setText("Phone = "+phone1);
            text_password.setText("Phone = "+password1);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(HomeActivity.this,"log out successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    public void ClickHome(View view){
        recreate();
    }

//    public void ClickMenu(View view){
//        //Open drawer
//        MainActivity.openDrawer(drawerLayout);
//    }
//
//    public void ClickLogo(View view){
//       MainActivity.closeDrawer(drawerLayout);
//    }

    public void ClickProfile (View view){
        MainActivity.redirectActivity(this, profil_activity.class);
    }


}