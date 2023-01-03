package master.pro.houssine.pfe.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import master.pro.houssine.pfe.Model.SharedPrefManger;
import master.pro.houssine.pfe.R;
import master.pro.houssine.pfe.Response.ResponseResetPassword;
import master.pro.houssine.pfe.Retrofit.InterfaceAPI;
import master.pro.houssine.pfe.Retrofit.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reset_passwordd extends AppCompatActivity {
    EditText new_password_txt;
    EditText confirm_new_password_txt;
    Button reset;
    Button annuler;
    String new_password;
    String confirm_new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwordd);

        new_password_txt = findViewById(R.id.new_password);
        confirm_new_password_txt = findViewById(R.id.confirme_password);
        reset = findViewById(R.id.reset);
        annuler = findViewById(R.id.annuler);

        SharedPreferences sharedpref1 = this.getSharedPreferences("MySharedPreference", MODE_PRIVATE);
        int user_id = sharedpref1.getInt("User_Id", 0);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_password = new_password_txt.getText().toString();
                confirm_new_password = confirm_new_password_txt.getText().toString();
                if (new_password.equals(confirm_new_password)) {
                    reset(new_password, user_id);
                } else {
                    Toast.makeText(Reset_passwordd.this, "Error typing, please reset  ", Toast.LENGTH_SHORT).show();
                    SharedPrefManger.getInstance(Reset_passwordd.this).clear();
                }


            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reset_passwordd.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void reset(String new_password, int id) {
        InterfaceAPI api = RetrofitClientInstance.getRetrofitInstance().create(InterfaceAPI.class);
        Call<ResponseResetPassword> call = api.updatePassword(new_password, id);

        call.enqueue(new Callback<ResponseResetPassword>() {

            @Override
            public void onResponse(Call<ResponseResetPassword> call, Response<ResponseResetPassword> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSucces()) {

                        Toast.makeText(Reset_passwordd.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        SharedPrefManger.getInstance(Reset_passwordd.this).clear();
                        Intent intent = new Intent(Reset_passwordd.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Reset_passwordd.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Reset_passwordd.this, "Invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResetPassword> call, Throwable t) {
            }
        });

    }


}