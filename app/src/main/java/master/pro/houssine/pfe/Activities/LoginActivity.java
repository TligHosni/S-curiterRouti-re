package master.pro.houssine.pfe.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseLoaclisation;
import master.pro.houssine.pfe.Response.ResponseLogin;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import master.pro.houssine.pfe.Utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText phone_txt;
    EditText password_txt;
    TextView forgetPassword;
    Button login;
    double longitude;
    double latitude;
    SharedPrefManger sharedPrefManger;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference myRef = db.getReference("Users");
    int verife = 0;
    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
//            fm.popBackStack();
            Intent intent = new Intent(LoginActivity.this, AcceuilActivity.class);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppUtils.getToken(getApplicationContext());
        login = (Button) findViewById(R.id.login);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);
        phone_txt = findViewById(R.id.phone);
        password_txt = findViewById(R.id.password);
        CheckBox simpleCheckBox = (CheckBox) findViewById(R.id.simpleCheckBox);

        sharedPrefManger = new SharedPrefManger(getApplicationContext());

        login.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String phone = phone_txt.getText().toString();
                String password = password_txt.getText().toString();
                myRef.setValue(phone, password);
                if(simpleCheckBox.isChecked()) {
                    login(phone, password);
                }else{
                    Toast.makeText(LoginActivity.this, "Accepter les règles de confidentialité", Toast.LENGTH_SHORT).show();
                }
            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                VerifPass changePassDialog = new VerifPass();
//                changePassDialog.show(getSupportFragmentManager(), "");

                startActivity(new Intent(LoginActivity.this,Code_verif.class));

            }

        });
    }


    private void login(String phone, String password) {
        SharedPrefManger sharedPrefManger = new SharedPrefManger(LoginActivity.this);
        String token = sharedPrefManger.getStringSaved(getApplicationContext(), "token");
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseLogin> call = api.login("94311010", "0000", token);

        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(@NonNull Call<ResponseLogin> call, @NonNull Response<ResponseLogin> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSucces()) {


//                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        sharedPrefManger.saveUser(response.body().getUser());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        int user_id = sharedPrefManger.getUser().getId();
                        updateLocalisation(user_id ,String.valueOf(latitude),String.valueOf(longitude));


                    } else {

                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseLogin> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocalisation(int id,String latitude, String longitude) {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseLoaclisation> call = api.updateLocalisation(id,latitude,longitude);

        call.enqueue(new Callback<ResponseLoaclisation>() {
            @Override
            public void onResponse(@NonNull Call<ResponseLoaclisation> call, @NonNull Response<ResponseLoaclisation> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseLoaclisation> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

